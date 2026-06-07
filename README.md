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

当前阶段：登录认证与权限校验已完成，下一阶段进入操作日志与数据字典。

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
- [x] 添加用户实体、DTO、VO
- [x] 添加用户 Mapper 和 XML SQL
- [x] 添加用户 Service
- [x] 添加用户 Controller
- [x] 完成用户分页查询、新增、修改、逻辑删除、状态启停
- [x] 添加部门实体、请求对象、VO
- [x] 添加部门 Mapper 和 XML SQL
- [x] 添加部门 Service
- [x] 添加部门 Controller
- [x] 完成部门树查询、新增、修改、删除校验
- [x] 添加角色实体、请求对象、查询对象、VO
- [x] 添加角色 Mapper、Service、Controller
- [x] 完成角色分页查询、新增、修改、删除、状态变更
- [x] 添加菜单实体、请求对象、VO
- [x] 添加菜单 Mapper、Service、Controller
- [x] 完成菜单树查询、新增、修改、删除校验
- [x] 完成用户分配角色
- [x] 完成角色分配菜单
- [x] 完成用户权限标识集合查询
- [x] 添加登录接口
- [x] 添加 JWT 生成、解析、校验
- [x] 添加 BCrypt 密码校验
- [x] 添加当前用户上下文
- [x] 添加登录认证拦截器
- [x] 添加 Redis token 黑名单
- [x] 添加 `@RequirePermission` 权限注解
- [x] 完成 401 / 403 错误处理

下一阶段：

- [ ] 操作日志注解
- [ ] AOP 记录操作日志
- [ ] 日志分页查询
- [ ] 字典类型管理
- [ ] 字典项管理
- [ ] Redis 缓存字典数据

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

当前项目已经创建 Spring Boot 基础骨架、通用基础能力、数据库初始化迁移、用户管理、部门管理、角色管理、菜单管理、RBAC 核心接口、登录认证和权限校验。

### 本地环境变量

开发环境数据库密码不写入代码。PowerShell 中先设置：

```powershell
$env:ADMIN_DB_PASSWORD="你的本机 MySQL root 密码"
```

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

`mvn test` 会验证 Spring Boot 上下文、统一响应体、分页响应体、业务异常、全局异常处理、基础配置、用户模块、部门模块、角色模块、菜单模块、RBAC 关联链路、登录认证、JWT、当前用户上下文、认证拦截、权限拦截和 token 黑名单。`mvn spring-boot:run` 会在开发环境连接 MySQL，并通过 Flyway 自动执行数据库迁移。

### 已有接口

认证接口：

```text
POST   /api/auth/login
GET    /api/auth/me
POST   /api/auth/logout
```

用户接口：

```text
GET    /api/users
GET    /api/users/{id}
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
PUT    /api/users/{id}/status
GET    /api/users/{id}/roles
PUT    /api/users/{id}/roles
GET    /api/users/{id}/permissions
```

部门接口：

```text
GET    /api/depts/tree
POST   /api/depts
PUT    /api/depts/{id}
DELETE /api/depts/{id}
```

角色接口：

```text
GET    /api/roles
GET    /api/roles/{id}
POST   /api/roles
PUT    /api/roles/{id}
DELETE /api/roles/{id}
PUT    /api/roles/{id}/status
GET    /api/roles/{id}/menus
PUT    /api/roles/{id}/menus
```

菜单接口：

```text
GET    /api/menus/tree
POST   /api/menus
PUT    /api/menus/{id}
DELETE /api/menus/{id}
```

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
