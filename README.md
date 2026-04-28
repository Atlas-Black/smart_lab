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
