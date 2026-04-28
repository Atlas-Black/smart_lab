# smart_lab

高校智能实验室资产管理系统。

本项目基于 Spring Boot 构建，围绕实验室设备管理、借用归还、并发安全、超时自动取消、操作审计与通知解耦等核心场景进行设计与实现。项目重点不只是完成基础 CRUD，而是通过 Redis、Redisson、RabbitMQ、AOP 和策略模式等技术，模拟更贴近企业级后端系统的设计思路。

---

## 项目简介

在高校实验室场景中，设备管理通常会遇到以下问题：

- 设备详情查询频繁，数据库压力较大
- 多人同时借用同一设备时，容易出现重复借用
- 用户预约设备后长时间未取用，会占用设备资源
- 关键操作缺少日志留痕，不方便审计和追踪
- 不同设备类型的通知逻辑容易堆积大量 if-else，维护成本高

本项目针对以上问题，逐步实现了：

- JWT 登录鉴权
- Redis 缓存查询
- EasyExcel 批量导入设备
- Redisson 分布式锁防超卖
- RabbitMQ 死信队列处理超时未取用
- AOP 无侵入式操作日志
- 策略模式重构通知系统

---

## 技术栈

- Java 21
- Spring Boot 3
- MyBatis-Plus
- MySQL
- Redis
- Redisson
- RabbitMQ
- EasyExcel
- Spring AOP
- JMeter
- Postman

---

## 核心功能

### 1. 登录鉴权
- 基于 JWT 实现登录认证
- 通过拦截器统一校验登录状态
- 使用 ThreadLocal 保存当前用户上下文

### 2. 设备管理
- 设备信息存储在 `lab_device` 表
- 支持设备详情查询
- 支持 Excel 批量导入设备数据

### 3. Redis 缓存查询
- 设备详情接口优先查询 Redis
- 缓存未命中时查询数据库并回填缓存
- 更新设备状态后删除缓存，保证缓存一致性

### 4. 借用管理
- 用户可借用可用设备
- 借用成功后生成 `borrow_record` 流水
- 借用后自动更新设备状态

### 5. 高并发防超卖
- 对同一设备 ID 加 Redisson 分布式锁
- 保证高并发场景下同一设备只能被借用一次

### 6. RabbitMQ 超时自动取消
- 借用成功后发送带 TTL 的消息
- 消息过期后进入死信队列
- 消费者检查借用状态，未取用则自动取消并释放设备

### 7. AOP 审计日志
- 通过自定义 `@AuditLog` 注解记录关键操作
- 自动保存操作人、模块名、操作类型、参数、时间等信息

### 8. 策略模式通知系统
- 根据设备价值等级动态选择通知策略
- 消除 if-else 分支
- 提高系统扩展性与可维护性

---

## 项目结构

```text
src/main/java/com/yiguan/smart_lab
├── annotation      // 自定义注解
├── aspect          // AOP 切面
├── common          // 统一返回体
├── config          // 配置类（Redis、RabbitMQ、MyBatis-Plus、Web）
├── context         // 用户上下文
├── controller      // 控制器
├── dto             // 请求参数对象
├── exception       // 全局异常处理
├── interceptor     // 登录拦截器
├── listener        // Excel / RabbitMQ 监听器
├── mapper          // MyBatis-Plus Mapper
├── model           // 实体类
├── service         // 业务接口
├── service/impl    // 业务实现
├── strategy        // 策略模式实现类
├── utils           // 工具类（JWT等）
└── vo              // 返回对象

‘’‘text

---
## 如何解决超卖问题（Redisson）

### 问题背景
在实验室设备借用场景中，如果多个用户同时请求借用同一台设备，可能都会先查询到设备状态为“可借用”，从而导致同一台设备被重复借出，也就是典型的超卖问题。

### 解决思路
本项目采用 **Redisson 分布式锁 + 状态二次校验** 的方式解决该问题：

1. 根据设备 ID 获取分布式锁  
2. 成功加锁后进入临界区  
3. 再次查询设备当前状态  
4. 只有状态为“可借用”时才允许借用  
5. 更新设备状态为“已借出”  
6. 写入借用流水 `borrow_record`  
7. 删除 Redis 缓存，保证缓存一致性  
8. 在 `finally` 中释放锁，避免死锁

### 关键代码思路
```java
RLock lock = redissonClient.getLock("lock:lab_device:" + deviceId);

boolean locked = false;
try {
    locked = lock.tryLock();
    if (!locked) {
        throw new BusinessException("当前设备借用人数过多，请稍后再试");
    }

    LabDevice device = labDeviceMapper.selectById(deviceId);
    if (device == null) {
        throw new BusinessException("设备不存在");
    }

    if (device.getStatus() == null || device.getStatus() != 0) {
        throw new BusinessException("该设备当前不可借用");
    }

    device.setStatus(2);
    labDeviceMapper.updateById(device);

    BorrowRecord record = new BorrowRecord();
    record.setUserId(UserContext.getUserId());
    record.setDeviceId(deviceId);
    record.setBorrowStatus(1);
    borrowRecordMapper.insert(record);

    redisTemplate.delete("lab_device:" + deviceId);

} finally {
    if (locked && lock.isHeldByCurrentThread()) {
        lock.unlock();
    }
}
