# AdminSystem

企业后台管理系统练习项目。

这个项目的目标不是堆功能，而是完整走一遍 Java 后端后台系统从 0 到 1 的工程过程：项目初始化、数据库设计、接口开发、权限认证、缓存、消息队列、测试、部署和文档整理。

## 项目目标

完成一个标准企业后台管理系统，覆盖：

- 用户管理
- 角色管理
- 菜单管理
- 部门管理
- RBAC 权限模型
- JWT 登录认证
- 操作日志审计
- 数据字典缓存
- Docker Compose 本地部署
- 单元测试和基础集成测试

## 技术栈

| 层面 | 技术 |
|------|------|
| JDK | Java 21 |
| 构建工具 | Maven 3.9.x |
| 后端框架 | Spring Boot 3.5.x |
| ORM | MyBatis |
| 数据库 | MySQL 8.4 |
| 缓存 | Redis 7.x |
| 消息队列 | RabbitMQ 3.13.x |
| 认证 | JWT + BCrypt |
| 接口文档 | Knife4j |
| 数据库迁移 | Flyway |
| 测试 | JUnit 5 + AssertJ |
| 部署 | Docker Compose |

## 当前进度

当前阶段：Spring Boot 项目骨架。

已完成：

- [x] 创建 Git 仓库
- [x] 绑定 GitHub 远程仓库
- [x] 添加基础 Git 配置文件
- [x] 添加项目规格文档
- [x] 创建 Spring Boot Maven 项目骨架
- [x] 添加基础依赖
- [x] 创建基础包结构
- [x] 添加应用配置文件
- [x] 验证 `mvn test`

下一阶段：

- [ ] 统一响应体 `Result`
- [ ] 分页响应体 `PageResult`
- [ ] 业务异常 `BusinessException`
- [ ] 全局异常处理 `GlobalExceptionHandler`

## 文档

| 文档 | 说明 |
|------|------|
| [AdminSystem-Spec.md](./AdminSystem-Spec.md) | 完整项目规格文档 |

本地学习笔记和路线图放在 `docs/` 目录中，该目录不上传到 GitHub。

## 开发流程

本项目按小阶段推进。每个阶段完成后：

```bash
git status
git add .
git commit -m "<type>: <summary>"
git push
```

提交信息示例：

```text
chore: initialize spring boot project
docs: update project roadmap
feat: add global response wrapper
test: add user service tests
```

## 本地启动

当前项目已经创建 Spring Boot 基础骨架。

### 运行测试

```bash
mvn test
```

### 启动应用

```bash
mvn spring-boot:run
```

启动后默认端口：

```text
http://localhost:8080
```

当前阶段还没有业务接口，只验证 Spring Boot 应用能启动。

## 目录规划

```text
AdminSystem/
├── AdminSystem-Spec.md
├── README.md
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/admin/
│   │   │   ├── common/
│   │   │   ├── config/
│   │   │   ├── security/
│   │   │   └── module/
│   │   │       ├── auth/
│   │   │       ├── user/
│   │   │       ├── role/
│   │   │       ├── menu/
│   │   │       ├── dept/
│   │   │       ├── log/
│   │   │       └── dict/
│   │   └── resources/
│   └── test/
│       ├── java/
│       └── resources/
└── docs/               # 本地学习笔记，不上传 GitHub
```
