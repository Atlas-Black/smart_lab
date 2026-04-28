# smart_lab

高校智能实验室资产管理系统。

这是一个基于 Spring Boot 的实验室设备管理项目，围绕“设备管理、借用归还、并发安全、超时自动取消、操作审计、通知解耦”等核心场景进行设计与实现。项目目标不仅是完成基础 CRUD，更重点体现企业级后端开发中的缓存、分布式锁、消息队列、AOP 与设计模式应用。

---

## 项目简介

在高校实验室场景中，设备通常存在以下问题：

- 设备信息查询频繁，数据库压力较大
- 多人同时借同一设备时，容易出现重复借用
- 预约后长时间未取用，会占用设备资源
- 关键操作缺少日志留痕，不方便审计
- 不同类型设备通知逻辑复杂，容易堆积 if-else

本项目围绕上述问题，构建了一个具备以下能力的实验室资产管理系统：

- 用户登录鉴权
- 设备详情缓存查询
- Excel 批量导入设备
- 设备借用与借用流水记录
- Redisson 分布式锁防超卖
- RabbitMQ 死信队列处理超时未取用
- AOP 无侵入式审计日志
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
- 使用拦截器统一校验登录状态
- 基于 ThreadLocal 保存当前用户上下文

### 2. 设备管理
- 设备信息存储在 `lab_device` 表
- 支持设备详情查询
- 支持 Excel 批量导入设备数据

### 3. Redis 缓存查询
- 设备详情查询接口优先走 Redis
- 未命中缓存时查询数据库并回填缓存
- 更新设备状态后删除缓存，保证缓存一致性

### 4. 借用管理
- 用户可借用可用设备
- 借用成功后生成 `borrow_record` 流水记录
- 借用后自动更新设备状态

### 5. Redisson 防超卖
- 对同一设备 ID 加分布式锁
- 保证高并发场景下同一设备只能被借用一次

### 6. RabbitMQ 延迟取消
- 借用成功后发送带 TTL 的消息
- 消息过期后进入死信队列
- 消费者检查借用状态，未取用则自动取消并释放设备

### 7. AOP 审计日志
- 通过自定义 `@AuditLog` 注解记录关键操作
- 自动保存操作人、模块名、操作类型、参数、时间等信息

### 8. 策略模式通知系统
- 根据设备价值等级动态选择通知策略
- 消除通知逻辑中的 if-else 分支
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
├── listener        // Excel / RabbitMQ 消费监听器
├── mapper          // MyBatis-Plus Mapper
├── model           // 实体类
├── service         // 业务接口
├── service/impl    // 业务实现
├── strategy        // 策略模式实现类
├── utils           // 工具类（JWT等）
└── vo              // 返回对象
