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

当前阶段：数据库与 Flyway 初始化迁移。

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
- [x] 添加统一响应体 `Result`
- [x] 添加分页响应体 `PageResult`
- [x] 添加业务异常 `BusinessException`
- [x] 添加全局异常处理 `GlobalExceptionHandler`
- [x] 添加参数校验异常处理
- [x] 添加 Web 层异常响应测试
- [x] 添加 Web、MyBatis、Redis、RabbitMQ 配置入口
- [x] 补全开发环境配置
- [x] 创建数据库 `admin_system`
- [x] 添加 Flyway 初始化迁移
- [x] 创建系统用户、角色、菜单、部门等基础表
- [x] 插入初始化部门、角色、用户、菜单和权限数据
- [x] 添加操作日志表
- [x] 添加数据字典表和初始字典数据
- [x] 验证 Flyway 自动建表和迁移记录

下一阶段：

- [ ] 用户实体、DTO、VO
- [ ] 用户 Mapper 和 XML SQL
- [ ] 用户 Service
- [ ] 用户 Controller
- [ ] 用户分页查询、新增、修改、逻辑删除
- [ ] 部门实体、Mapper、Service、Controller
- [ ] 部门树查询和删除校验

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

当前项目已经创建 Spring Boot 基础骨架、通用基础能力和数据库初始化迁移。

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

当前阶段还没有业务接口。`mvn test` 会验证 Spring Boot 上下文、统一响应体、分页响应体、业务异常、全局异常处理和基础配置入口。`mvn spring-boot:run` 会在开发环境连接 MySQL，并通过 Flyway 自动执行数据库迁移。

## 目录规划

```text
AdminSystem/
├── AdminSystem-Spec.md
├── README.md
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/admin/
│   │   │   ├── common/      # 统一响应、分页响应、业务异常、全局异常
│   │   │   ├── config/      # Web、MyBatis、Redis、RabbitMQ 配置
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
│   │       └── db/migration/      # Flyway 数据库迁移脚本
│   └── test/
│       ├── java/
│       └── resources/
└── docs/               # 本地学习笔记，不上传 GitHub
```
