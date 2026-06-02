# 企业后台管理系统 · 完整项目规格文档

## 1. 项目概述

### 1.1 项目定位

一个标准的企业级后台管理系统，覆盖 RBAC 权限模型、操作日志审计、数据字典缓存。目标是补齐 Java 后端实习生的基本功——MySQL + MyBatis + JWT + RBAC + AOP + Docker。

不追求标新立异，追求**每个模块都能在面试中讲清楚三件事**：数据结构怎么设计、核心 SQL 怎么写、为什么这么做。

### 1.2 技术栈

| 层面 | 技术 | 版本 | 为什么选这个 |
|------|------|------|-------------|
| 语言 | Java | 21（本机 Oracle JDK 21.0.6） | LTS；本机已安装。若要兼容更保守的面试/部署环境，可在 Maven 中配置 `maven.compiler.release=17` |
| 构建 | Maven | 3.9.11（本机） | 本机已安装，适合 Spring Boot 项目管理依赖和插件 |
| 框架 | Spring Boot | 3.5.x | 兼容 Java 21，生态成熟；暂不追 Spring Boot 4，避免 Knife4j、PageHelper 等第三方依赖兼容性风险 |
| ORM | MyBatis Spring Boot Starter | 3.0.x | JD 出现频率高，保留手写 SQL 的面试价值 |
| 分页 | PageHelper Spring Boot Starter | 4.x | MyBatis 生态常用分页插件，注意 starter 版本和 PageHelper core 版本不是同一个概念 |
| 数据库 | MySQL | 8.4 LTS（本机 8.4.4） | 本机已安装；8.4 是当前更适合长期使用的 MySQL LTS 分支 |
| 缓存 | Redis | 7.x（本机未检测到命令） | 字典数据、权限集合、JWT 黑名单缓存；本机建议通过 Docker 或单独安装 |
| 认证 | jjwt | 0.13.x | 轻量，不引入 Spring Security 全家桶 |
| 密码 | BCrypt | (Spring Security Crypto) | 只引 crypto，不加 Security 配置链 |
| 接口文档 | Knife4j | 4.x | Swagger 增强，比原版 Swagger UI 好用 |
| 参数校验 | Spring Validation | (Spring Boot 内置) | @Valid + @NotBlank 等 |
| 工具 | Hutool | 5.8.x | 国内最常用的 Java 工具库 |
| MQ | RabbitMQ | 3.13.x（本机未检测到命令） | 操作日志异步写入；3.13 系列资料多、兼容性稳 |
| 部署 | Docker Compose v2（本机未检测到 Docker） | MySQL + Redis + RabbitMQ + App 一键启动；当前机器需要先安装 Docker Desktop |
| 测试 | JUnit 5 + AssertJ | (Spring Boot 内置) | 对关键 Service 写测试 |

### 1.2.1 本机环境核对（2026-06-02）

| 项目 | 本机检测结果 | 对项目的影响 |
|------|-------------|-------------|
| Java | `java version "21.0.6"`，Oracle JDK，路径经 `javapath` 暴露 | 项目可直接使用 Java 21；如果希望面试官机器更容易运行，可在 `pom.xml` 用 `maven.compiler.release=17` |
| Maven | Apache Maven 3.9.11，路径 `D:\apache-maven\apache-maven-3.9.11` | 可直接执行 `mvn test`、`mvn spring-boot:run` |
| MySQL | MySQL Community Server 8.4.4，路径 `C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe` | 文档和 Docker 示例建议从 MySQL 8.0 调整为 8.4 LTS |
| Docker | PATH 中未检测到 `docker` | Docker Compose 一键启动是目标能力，但本机需要先安装 Docker Desktop |
| Redis | PATH 中未检测到 `redis-server` | 推荐通过 Docker Compose 启动，或单独安装 Redis for Windows/WSL |
| RabbitMQ | PATH 中未检测到 `rabbitmqctl` | 推荐通过 Docker Compose 启动，避免手工安装 Erlang/RabbitMQ |
| Node.js | v22.17.0 | 当前后端项目暂不需要；如果后续补前端管理台可复用 |
| npm | PowerShell 执行策略阻止 `npm.ps1` | 暂不影响后端；需要前端时可用 `npm.cmd` 或调整执行策略 |

### 1.2.2 设计改进建议

| 改进点 | 建议 | 原因 |
|--------|------|------|
| 版本策略 | 运行环境用 Java 21，源码兼容目标可按需要设为 17；Spring Boot 选 3.5.x | 本机环境新，但求职项目要兼顾可运行性和依赖生态稳定性 |
| 数据库变更管理 | 增加 Flyway，建表 SQL 放 `src/main/resources/db/migration/V1__init_schema.sql` | 比手工执行 `docs/schema.sql` 更工程化，面试能讲版本化迁移 |
| 菜单/部门树组装 | 用 `Map<parentId, children>` 分组后递归，避免每层递归都扫描全量列表 | 原写法是 O(n²)，数据量稍大就低效；分组后接近 O(n) |
| 索引和唯一约束 | 给关联表反向字段、树表 `parent_id`、常用查询字段加索引 | RBAC 查询和树查询是高频路径，索引能体现数据库基本功 |
| 权限缓存失效 | 用户角色、角色菜单、菜单权限变更时主动删除相关用户权限缓存 | 只设置过期时间不够，权限变更需要尽快生效 |
| 操作日志 MQ | 先实现同步入库，再在 Phase 4 改为 RabbitMQ 异步 | 降低早期复杂度，也方便定位 AOP 逻辑是否正确 |
| 接口文档兼容性 | 保留 Knife4j，但若升级 Spring Boot 4 时优先验证 springdoc/Knife4j 兼容性 | 第三方 Swagger UI 组件常受 Spring Boot 大版本升级影响 |
| 测试策略 | Service 单元测试 + 关键 Mapper 集成测试；Docker 可用后再补 Testcontainers | 只测 Service 不够覆盖 SQL，Mapper 测试能证明手写 SQL 正确 |

### 1.3 不引入的东西及理由

| 不引入 | 理由 |
|--------|------|
| Spring Security | 配置链太复杂，大三不需要。JWT 拦截器手写，面试能聊得更深 |
| MyBatis-Plus | 先手写 SQL 理解底层，之后再学 Plus 是水到渠成；简历上写"手写 SQL"比"用 MyBatis-Plus"有说服力 |
| Shiro | 已过时，新项目几乎不用 |
| Nacos / 注册中心 | 单机项目不需要服务发现 |
| 前端框架 | 这不是前端项目，用 Knife4j 自带的 Swagger UI 调试即可 |

---

## 2. 数据库设计

### 2.1 ER 图（文字描述）

```
user ──< user_role >── role ──< role_menu >── menu
 │                       │
 └── dept                └── (独立)
```

- 用户 ↔ 角色：多对多，通过 `user_role` 关联
- 角色 ↔ 菜单：多对多，通过 `role_menu` 关联
- 用户 → 部门：多对一，`user.dept_id` 外键
- 菜单：自关联树形结构，`parent_id` 指向同一表
- 字典：`dict_type` 1→N `dict_item`

### 2.2 建表 DDL

```sql
-- ==================== 用户表 ====================
CREATE TABLE sys_user (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(200) NOT NULL           COMMENT '密码(BCrypt)',
    nickname    VARCHAR(50)                     COMMENT '昵称',
    email       VARCHAR(100)                    COMMENT '邮箱',
    phone       VARCHAR(20)                     COMMENT '手机号',
    gender      TINYINT      DEFAULT 0          COMMENT '0未知 1男 2女',
    avatar      VARCHAR(500)                    COMMENT '头像URL',
    dept_id     BIGINT       DEFAULT 0          COMMENT '部门ID',
    status      TINYINT      DEFAULT 1          COMMENT '1启用 0禁用',
    create_by   VARCHAR(50)                     COMMENT '创建人',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_by   VARCHAR(50)                     COMMENT '更新人',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted     TINYINT      DEFAULT 0          COMMENT '0未删除 1已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ==================== 角色表 ====================
CREATE TABLE sys_role (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL           COMMENT '角色名称',
    code        VARCHAR(50)  NOT NULL UNIQUE    COMMENT '角色编码(如admin/editor)',
    description VARCHAR(200)                    COMMENT '描述',
    status      TINYINT      DEFAULT 1          COMMENT '1启用 0禁用',
    sort_order  INT          DEFAULT 0          COMMENT '排序',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- ==================== 用户-角色关联表 ====================
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';

-- ==================== 菜单表 ====================
CREATE TABLE sys_menu (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    parent_id   BIGINT       DEFAULT 0           COMMENT '父菜单ID(0=顶级)',
    name        VARCHAR(50)  NOT NULL            COMMENT '菜单名称',
    type        TINYINT      NOT NULL            COMMENT '1目录 2菜单 3按钮',
    path        VARCHAR(200)                     COMMENT '路由路径',
    component   VARCHAR(200)                     COMMENT '组件路径',
    permission  VARCHAR(200)                     COMMENT '权限标识(sys:user:add)',
    icon        VARCHAR(100)                     COMMENT '图标',
    sort_order  INT          DEFAULT 0           COMMENT '排序',
    visible     TINYINT      DEFAULT 1           COMMENT '1可见 0隐藏',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- ==================== 角色-菜单关联表 ====================
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单关联表';

-- ==================== 部门表 ====================
CREATE TABLE sys_dept (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    parent_id   BIGINT       DEFAULT 0           COMMENT '父部门ID(0=顶级)',
    name        VARCHAR(50)  NOT NULL            COMMENT '部门名称',
    leader      VARCHAR(50)                      COMMENT '负责人',
    phone       VARCHAR(20)                      COMMENT '联系电话',
    sort_order  INT          DEFAULT 0           COMMENT '排序',
    status      TINYINT      DEFAULT 1           COMMENT '1启用 0禁用',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统部门表';

-- ==================== 操作日志表 ====================
CREATE TABLE sys_log (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)                      COMMENT '操作用户',
    module      VARCHAR(100)                     COMMENT '操作模块(用户管理/角色管理)',
    operation   VARCHAR(100)                     COMMENT '操作描述(新增用户/修改角色)',
    method      VARCHAR(200)                     COMMENT '请求方法(com.example.UserController.save)',
    request_url VARCHAR(200)                     COMMENT '请求URL',
    request_ip  VARCHAR(50)                      COMMENT '请求IP',
    params      TEXT                             COMMENT '请求参数(JSON)',
    result      TEXT                             COMMENT '返回结果(JSON)',
    duration    BIGINT                           COMMENT '耗时(ms)',
    status      TINYINT      DEFAULT 1           COMMENT '1成功 0失败',
    error_msg   TEXT                             COMMENT '异常信息',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

-- 日志表索引
CREATE INDEX idx_sys_log_username ON sys_log(username);
CREATE INDEX idx_sys_log_create_time ON sys_log(create_time);
CREATE INDEX idx_sys_log_module ON sys_log(module);

-- ==================== 字典类型表 ====================
CREATE TABLE sys_dict_type (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL            COMMENT '字典名称',
    code        VARCHAR(50)  NOT NULL UNIQUE     COMMENT '字典编码(gender/status)',
    description VARCHAR(200)                     COMMENT '描述',
    status      TINYINT      DEFAULT 1           COMMENT '1启用 0禁用',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- ==================== 字典项表 ====================
CREATE TABLE sys_dict_item (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    type_id     BIGINT       NOT NULL            COMMENT '字典类型ID',
    label       VARCHAR(50)  NOT NULL            COMMENT '字典标签(男/女)',
    value       VARCHAR(50)  NOT NULL            COMMENT '字典值(1/2)',
    color       VARCHAR(20)                      COMMENT '标签颜色(前端用)',
    sort_order  INT          DEFAULT 0           COMMENT '排序',
    status      TINYINT      DEFAULT 1           COMMENT '1启用 0禁用',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典项表';

CREATE INDEX idx_sys_dict_item_type ON sys_dict_item(type_id);

-- ==================== 常用查询/关联索引建议 ====================
CREATE INDEX idx_sys_user_dept ON sys_user(dept_id);
CREATE INDEX idx_sys_user_status_deleted ON sys_user(status, deleted);
CREATE INDEX idx_sys_role_status ON sys_role(status);
CREATE INDEX idx_sys_menu_parent ON sys_menu(parent_id);
CREATE INDEX idx_sys_dept_parent ON sys_dept(parent_id);
CREATE INDEX idx_sys_user_role_role ON sys_user_role(role_id);
CREATE INDEX idx_sys_role_menu_menu ON sys_role_menu(menu_id);
CREATE UNIQUE INDEX uk_sys_dict_item_type_value ON sys_dict_item(type_id, value);
```

### 2.3 建表完毕后的初始数据

```sql
-- 初始管理员
INSERT INTO sys_user (username, password, nickname, status) VALUES
('admin', '$2a$10$xxxxx', '超级管理员', 1);  -- BCrypt 加密 "admin123"

-- 初始角色
INSERT INTO sys_role (name, code, description, sort_order) VALUES
('超级管理员', 'admin', '拥有所有权限', 1),
('普通用户',   'user',  '基础权限',     2);

-- 初始部门
INSERT INTO sys_dept (id, parent_id, name, leader, sort_order) VALUES
(1, 0, '总公司', 'CEO', 1),
(2, 1, '技术部', 'CTO', 2),
(3, 1, '产品部', 'CPO', 3);

-- 初始字典类型
INSERT INTO sys_dict_type (name, code, description) VALUES
('用户性别', 'gender', '0未知 1男 2女');
```

---

## 3. 模块详细设计

### 3.1 统一响应体

```java
public record Result<T>(
    int code,
    String message,
    T data
) {
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(200, "success", null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }
}

public record PageResult<T>(
    long total,
    List<T> rows
) {}
```

---

### 3.2 用户管理模块

#### 接口列表

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/users` | 新增用户 |
| DELETE | `/api/users/{id}` | 删除用户（逻辑删除） |
| PUT | `/api/users/{id}` | 更新用户 |
| GET | `/api/users/{id}` | 查询单个用户 |
| GET | `/api/users` | 分页查询 `?page=1&size=10&username=&status=&deptId=` |
| PUT | `/api/users/{id}/password` | 重置密码 |
| PUT | `/api/users/{id}/status` | 启用/禁用 |
| GET | `/api/users/{id}/roles` | 查询用户的角色 |
| PUT | `/api/users/{id}/roles` | 分配角色（全量替换） |

#### 核心 MyBatis XML

```xml
<!-- UserMapper.xml: 分页查询 + 关联部门名称 -->
<select id="findPage" resultType="com.example.admin.module.user.UserVO">
    SELECT
        u.id, u.username, u.nickname, u.email, u.phone,
        u.gender, u.status, u.dept_id, u.create_time,
        d.name AS dept_name
    FROM sys_user u
    LEFT JOIN sys_dept d ON u.dept_id = d.id
    WHERE u.deleted = 0
    <if test="username != null and username != ''">
        AND u.username LIKE CONCAT('%', #{username}, '%')
    </if>
    <if test="status != null">
        AND u.status = #{status}
    </if>
    <if test="deptId != null">
        AND u.dept_id = #{deptId}
    </if>
    ORDER BY u.create_time DESC
</select>
```

#### 面试关键点

- **密码存储**：BCrypt 加密，`BCryptPasswordEncoder.encode(rawPassword)`，不可逆
- **逻辑删除**：`deleted=1`，查询时永远带 `WHERE deleted=0`
- **分页**：PageHelper 的 `startPage(page, size)` 在实际查询前调用，ThreadLocal 传参
- **VO vs 实体**：`UserVO` 包含 `deptName`（关联查询），`User` 实体只有 `deptId`

---

### 3.3 角色管理模块

#### 接口列表

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/roles` | 新增角色 |
| DELETE | `/api/roles/{id}` | 删除角色 |
| PUT | `/api/roles/{id}` | 更新角色 |
| GET | `/api/roles/{id}` | 查询单个角色 |
| GET | `/api/roles` | 分页查询 |
| GET | `/api/roles/{id}/menus` | 查询角色拥有的菜单ID列表 |
| PUT | `/api/roles/{id}/menus` | 分配菜单权限（全量替换） |

#### 分配权限的实现

```java
@Transactional
public void assignMenus(Long roleId, List<Long> menuIds) {
    // 1. 删除角色原有的所有菜单关联
    roleMapper.deleteRoleMenus(roleId);
    // 2. 批量插入新的
    if (!menuIds.isEmpty()) {
        roleMapper.insertRoleMenus(roleId, menuIds);
    }
}
```

#### 面试关键点

- **`@Transactional`** 保证删除+插入的原子性
- **全量替换 vs 增量更新**：全量替换实现简单，前端传完整列表，后端不用 diff
- **数据权限和菜单权限的区别**：菜单权限控制"能看到哪些页面"，数据权限控制"能看到哪些数据行"

---

### 3.4 菜单管理模块

#### 接口列表

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/menus` | 查询全部菜单（树形结构返回） |
| POST | `/api/menus` | 新增菜单 |
| PUT | `/api/menus/{id}` | 更新菜单 |
| DELETE | `/api/menus/{id}` | 删除菜单（有子菜单不能删） |
| GET | `/api/menus/tree` | 树形数据（前端用） |

#### 树形数据组装

```java
// 核心方法：把平铺列表转成树
public List<MenuTreeVO> buildTree(List<Menu> menus) {
    // 先按 parentId 分组，再递归组装，避免递归时反复扫描全量列表。
    Map<Long, List<Menu>> childrenMap = menus.stream()
        .collect(Collectors.groupingBy(Menu::getParentId));

    return childrenMap.getOrDefault(0L, List.of()).stream()
        .sorted(Comparator.comparing(Menu::getSortOrder))
        .map(root -> convertToTree(root, childrenMap))
        .toList();
}

private MenuTreeVO convertToTree(Menu menu, Map<Long, List<Menu>> childrenMap) {
    List<MenuTreeVO> children = childrenMap.getOrDefault(menu.getId(), List.of()).stream()
        .sorted(Comparator.comparing(Menu::getSortOrder))
        .map(child -> convertToTree(child, childrenMap))
        .toList();
    return new MenuTreeVO(menu, children);
}
```

#### 面试关键点

- **树形数据建模**：`parent_id` 自关联是最通用的方式
- **递归在 Java 层做而不是 SQL 层**：一次查出菜单列表后，用 `Map<parentId, children>` 组装树，复杂度接近 O(n)，也方便做缓存
- **type 字段的三个值**：目录（展开/折叠）、菜单（对应一个页面）、按钮（对应一个操作权限，如"新增用户"按钮）
- **权限标识 `permission` 的格式**：`sys:user:add`，这是 RBAC 中"权限"的最小粒度

---

### 3.5 部门管理模块

#### 接口列表

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/depts` | 查询全部部门（树形） |
| POST | `/api/depts` | 新增部门 |
| PUT | `/api/depts/{id}` | 更新部门 |
| DELETE | `/api/depts/{id}` | 删除部门（有子部门或有关联用户时不能删） |

#### 面试关键点

- **树形 + 业务约束**：删除时检查 `parent_id` 指向本部门的子部门是否存在，以及 `sys_user.dept_id` 是否有用户关联
- **和菜单树的区别**：部门树不需要 `type` 字段，但有 `leader`、`phone` 等业务字段——树形结构是骨架，业务字段是肉

---

### 3.6 登录认证模块

#### 接口列表

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 登录 `{username, password}` → `{token, expiresIn}` |
| POST | `/api/auth/logout` | 退出登录（token 加入 Redis 黑名单） |
| GET | `/api/auth/me` | 获取当前登录用户信息 |
| PUT | `/api/auth/password` | 修改自己的密码（需旧密码验证） |

#### 完整登录流程

```
┌──────┐     ┌──────────┐     ┌────────┐     ┌───────┐
│ 前端  │     │ LoginController│  │ AuthService│  │  Redis  │
└──┬───┘     └─────┬────┘     └─────┬──┘     └───┬───┘
   │  POST /login   │              │            │
   │  {user, pass}  │              │            │
   │───────────────>│              │            │
   │                │  authenticate│            │
   │                │─────────────>│            │
   │                │              │ 查用户表    │
   │                │              │ BCrypt校验  │
   │                │              │ 查用户角色   │
   │                │              │ 查角色菜单   │
   │                │              │ 生成JWT     │
   │                │              │ 存Redis    │
   │                │              │───────────>│
   │                │  {token}     │            │
   │  {token}       │<─────────────│            │
   │<───────────────│              │            │
   │                │              │            │
   │  后续请求带      │              │            │
   │  Authorization │              │            │
   │───────────────>│              │            │
   │     JwtAuthInterceptor 拦截   │            │
   │     解析token → 查Redis黑名单 → 校验通过 → 放行
```

#### JWT Token 设计

```json
{
  "sub": "1",            // 用户ID
  "username": "admin",
  "nickname": "超级管理员",
  "dept_id": 1,
  "iat": 1700000000,     // 签发时间
  "exp": 1700086400      // 过期时间（24小时后）
}
```

#### 权限校验流程

```
请求 → JwtAuthInterceptor
         → 解析 token，得到用户ID
         → 查 Redis 黑名单（token 是否被登出）
         → 查 Redis 缓存：该用户的权限集合 {sys:user:add, sys:user:del, ...}
            ├── 未命中 → 查 DB（user → user_role → role_menu → menu.permission）
            │              → 写入 Redis 缓存
            └── 命中   → 直接使用
         → 检查当前请求的权限标识是否在集合中
         → 通过：放行  /  不通过：返回 403
```

```java
// 权限校验注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    String value();  // 如 "sys:user:add"
}

// 拦截器中校验
// 从 UserContext (ThreadLocal) 获取当前用户权限集合
// 匹配方法上的 @RequirePermission 注解
// 不在集合中 → 返回 403
```

#### JWT 核心代码

```java
@Component
public class JwtTokenProvider {

    private final SecretKey key = Jwts.SIG.HS256.key().build();  // 生产环境应从配置读取
    private final long expiration = 24 * 60 * 60 * 1000L;        // 24小时

    public String generateToken(Long userId, String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(key)
                .compact();
    }

    public Long getUserId(String token) {
        return Long.parseLong(Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
```

#### 面试关键点

- **JWT vs Session**：无状态、不依赖服务端存储、适合分布式；但无法服务端主动失效（通过 Redis 黑名单弥补）
- **为什么不用 Spring Security**：太重，手写拦截器能理解每个步骤——面试官如果追问你为什么不用，正好展示你的选型思考
- **密码存储**：BCrypt 自带盐值，同一密码每次加密结果不同
- **Token 刷新策略**：过期前 30 分钟内访问 → 自动续期（可选实现）
- **ThreadLocal**：`UserContext` 存储当前请求用户，请求结束后必须 `remove()`（防止内存泄漏和线程池复用导致的串号）

---

### 3.7 操作日志模块

#### 设计要点

- 使用 **AOP 切面**拦截 Controller 方法
- 使用 **RabbitMQ** 异步写入日志表（不阻塞业务接口）
- 日志包含：操作人、模块、操作描述、请求参数、返回结果、IP、耗时

#### AOP 注解

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLog {
    String module() default "";      // 操作模块
    String operation() default "";   // 操作描述
}
```

#### 切面实现核心

```java
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Around("@annotation(sysLog)")
    public Object around(ProceedingJoinPoint point, SysLog sysLog) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;
        Throwable error = null;
        try {
            result = point.proceed();
        } catch (Throwable t) {
            error = t;
            throw t;
        } finally {
            long duration = System.currentTimeMillis() - start;

            SysLogEntry entry = SysLogEntry.builder()
                .username(getCurrentUsername())
                .module(sysLog.module())
                .operation(sysLog.operation())
                .method(point.getSignature().toLongString())
                .params(JSON.toJSONString(point.getArgs()))
                .duration(duration)
                .status(error == null ? 1 : 0)
                .errorMsg(error == null ? null : error.getMessage())
                .build();

            // 异步发送到 MQ，由消费者入库
            rabbitTemplate.convertAndSend("sys.log.exchange", "sys.log", entry);
        }
        return result;  // unreachable if error thrown, but keeps compiler happy
    }

    private String getCurrentUsername() {
        try {
            return UserContext.get().getUsername();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
```

#### MQ 消费者

```java
@Component
public class SysLogConsumer {

    @Autowired
    private SysLogMapper sysLogMapper;

    @RabbitListener(queues = "sys.log.queue")
    public void handle(SysLogEntry entry) {
        sysLogMapper.insert(entry.toEntity());
    }
}
```

#### 面试关键点

- **为什么用 MQ 异步写入**：日志不是业务主流程，同步写库会拖慢接口响应
- **MQ 消息丢失问题**：对于日志场景可以容忍少量丢失，如果需要高可靠，开启持久化和手动 ACK
- **AOP 获取不到参数怎么办**：JSON 序列化可能失败（参数不可序列化、大文件上传等），要 try-catch 兜底
- **从 UserContext（ThreadLocal）获取当前用户**：请求进入时由拦截器设置，离开时由拦截器清除

---

### 3.8 数据字典模块

#### 接口列表

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/dict/types` | 字典类型分页 |
| POST | `/api/dict/types` | 新增字典类型 |
| PUT | `/api/dict/types/{id}` | 更新字典类型 |
| DELETE | `/api/dict/types/{id}` | 删除字典类型（级联删除字典项） |
| GET | `/api/dict/items?typeCode={code}` | 按编码查字典项列表 |
| POST | `/api/dict/items` | 新增字典项 |
| PUT | `/api/dict/items/{id}` | 更新字典项 |
| DELETE | `/api/dict/items/{id}` | 删除字典项 |

#### 缓存策略

```java
@Service
public class DictService {

    @Autowired
    private DictItemMapper dictItemMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CACHE_PREFIX = "dict:";

    public List<DictItem> getItemsByTypeCode(String typeCode) {
        // 1. 查 Redis
        String cacheKey = CACHE_PREFIX + typeCode;
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return JSON.parseArray(cached, DictItem.class);
        }

        // 2. 查 DB
        List<DictItem> items = dictItemMapper.selectByTypeCode(typeCode);

        // 3. 写 Redis (过期时间 1 小时)
        redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(items), 1, TimeUnit.HOURS);

        return items;
    }

    // 增删改时清除缓存
    public void clearCache(String typeCode) {
        redisTemplate.delete(CACHE_PREFIX + typeCode);
    }
}
```

#### 面试关键点

- **为什么字典数据适合缓存**：数据量小、查询频繁、变更极少
- **缓存更新策略**：先更新 DB，再删除缓存（Cache Aside 模式），不直接更新缓存（会有并发问题）
- **缓存穿透**：不合法的 typeCode 不会打到 DB，因为 Redis 存的是缓存未命中标记也可以
- **缓存雪崩**：不同字典项的过期时间加随机偏移（当前是统一 1 小时，可优化）

---

### 3.9 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return Result.fail(400, msg);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleUnknown(Exception e) {
        log.error("unhandled exception", e);
        return Result.fail(500, "服务器内部错误");
    }
}
```

---

## 4. 包结构

```
com.example.admin
│
├── AdminApplication.java              (启动类)
│
├── common
│   ├── Result.java                    (统一响应体)
│   ├── PageResult.java                (分页响应体)
│   └── BusinessException.java         (业务异常)
│
├── config
│   ├── WebConfig.java                 (CORS、拦截器注册)
│   ├── RedisConfig.java               (Redis 序列化)
│   ├── MyBatisConfig.java             (分页插件、驼峰映射)
│   └── RabbitMQConfig.java            (队列/交换机/绑定)
│
├── security
│   ├── JwtTokenProvider.java          (生成/校验/解析 token)
│   ├── JwtAuthInterceptor.java        (拦截器，校验 token + 权限)
│   ├── LoginUser.java                 (登录用户信息 VO)
│   ├── UserContext.java               (ThreadLocal 存储当前用户)
│   └── RequirePermission.java         (权限校验注解)
│
├── module
│   ├── auth
│   │   ├── AuthController.java        (登录/登出/当前用户)
│   │   └── AuthService.java
│   │
│   ├── user
│   │   ├── UserController.java
│   │   ├── UserService.java
│   │   ├── UserMapper.java
│   │   ├── UserMapper.xml
│   │   ├── User.java                  (实体)
│   │   ├── UserVO.java                (视图对象，含 deptName)
│   │   ├── UserQueryDTO.java          (查询条件)
│   │   └── UserSaveDTO.java           (新增/编辑请求体)
│   │
│   ├── role
│   │   ├── RoleController.java
│   │   ├── RoleService.java
│   │   ├── RoleMapper.java
│   │   ├── RoleMapper.xml
│   │   └── Role.java
│   │
│   ├── menu
│   │   ├── MenuController.java
│   │   ├── MenuService.java
│   │   ├── MenuMapper.java
│   │   ├── MenuMapper.xml
│   │   ├── Menu.java
│   │   └── MenuTreeVO.java
│   │
│   ├── dept
│   │   ├── DeptController.java
│   │   ├── DeptService.java
│   │   ├── DeptMapper.java
│   │   ├── DeptMapper.xml
│   │   ├── Dept.java
│   │   └── DeptTreeVO.java
│   │
│   ├── log
│   │   ├── SysLog.java                (注解)
│   │   ├── SysLogAspect.java          (AOP 切面)
│   │   ├── SysLogEntry.java           (MQ 消息体)
│   │   ├── SysLogConsumer.java        (MQ 消费者)
│   │   ├── SysLogService.java
│   │   ├── SysLogController.java      (日志查询接口)
│   │   ├── SysLogMapper.java
│   │   └── SysLogMapper.xml
│   │
│   └── dict
│       ├── DictTypeController.java
│       ├── DictItemController.java
│       ├── DictService.java
│       ├── DictTypeMapper.java
│       ├── DictItemMapper.java
│       ├── DictTypeMapper.xml
│       ├── DictItemMapper.xml
│       ├── DictType.java
│       └── DictItem.java
│
└── resources
    ├── application.yml                (通用配置)
    ├── application-dev.yml            (开发环境)
    └── mapper/                        (MyBatis XML 文件)
        ├── UserMapper.xml
        ├── RoleMapper.xml
        ├── MenuMapper.xml
        ├── DeptMapper.xml
        ├── SysLogMapper.xml
        ├── DictTypeMapper.xml
        └── DictItemMapper.xml
```

---

## 5. 迭代计划（3 周）

### 总原则

- 每个 Phase 做完要能跑、能调、能演示
- 每个 Day 至少一个 commit
- 先明确接口契约和关键测试场景，再实现 Service/Mapper，最后补全文档

---

### Phase 0：项目初始化（Day 1-2）

#### Day 1：搭骨架

- [ ] Spring Initializr 创建项目：Web、MyBatis、MySQL Driver、Redis、Validation、Actuator
- [ ] pom.xml 补依赖：jjwt、PageHelper、Knife4j、Hutool、AMQP (RabbitMQ)、bcrypt、Flyway
- [ ] 配 application-dev.yml：数据源、Redis、RabbitMQ 连接信息
- [ ] 创建所有包目录
- [ ] 把 DDL 放入 `src/main/resources/db/migration/V1__init_schema.sql`，由 Flyway 自动迁移并插入初始数据
- [ ] 验证：启动项目，能连上数据库

#### Day 2：基础设施

- [ ] `Result` + `PageResult` 统一响应体
- [ ] `BusinessException` 业务异常
- [ ] `GlobalExceptionHandler` 全局异常处理
- [ ] `WebConfig`（CORS 跨域）
- [ ] `MyBatisConfig`（PageHelper + 驼峰映射）
- [ ] `RedisConfig`（序列化配置）
- [ ] `RabbitMQConfig`（队列交换机声明）
- [ ] 验证：Postman 调一个不存在的接口，返回统一 JSON 格式错误

**检查点**：项目能启动，有统一异常处理，表建好了

---

### Phase 1：用户 + 部门模块（Day 3-6）

#### Day 3-4：用户 CRUD

- [ ] `User` 实体
- [ ] `UserMapper` + `UserMapper.xml`（insert / update / delete / selectById / findPage）
- [ ] `UserService`（CRUD + 分页 + 模糊搜索 + 逻辑删除）
- [ ] `UserController`（REST 接口）
- [ ] `UserVO`（含 deptName 的联表查询）

#### Day 5：部门管理

- [ ] `Dept` 实体
- [ ] `DeptMapper` + `DeptMapper.xml`
- [ ] `DeptService`（CRUD + 树形组装 + 删除校验）
- [ ] `DeptController`
- [ ] `DeptTreeVO`

#### Day 6：参数校验 + 接口文档

- [ ] 给所有 Controller 加 `@Valid`
- [ ] 给 DTO 加校验注解（`@NotBlank`、`@NotNull`）
- [ ] Knife4j 配置，生成接口文档
- [ ] 验证：访问 `http://localhost:8080/doc.html` 能看到所有接口

**检查点**：用户增删改查能用 Postman 调通，部门树能用 Postman 看到

---

### Phase 2：角色 + 菜单 + RBAC（Day 7-11）

#### Day 7-8：角色管理

- [ ] `Role` 实体
- [ ] `RoleMapper` + `RoleMapper.xml`
- [ ] `RoleService`（CRUD）
- [ ] `RoleController`
- [ ] 用户-角色关联：`UserService.assignRoles(userId, roleIds)`

#### Day 9-10：菜单管理

- [ ] `Menu` 实体
- [ ] `MenuMapper` + `MenuMapper.xml`
- [ ] `MenuService`（CRUD + 树形组装 + 递归找子节点）
- [ ] `MenuController`
- [ ] `MenuTreeVO`

#### Day 11：角色-菜单权限关联

- [ ] 角色-菜单关联表操作：`RoleService.assignMenus(roleId, menuIds)`
- [ ] 查询角色拥有的菜单ID列表
- [ ] 查询角色拥有的权限标识列表（`permission` 字段）
- [ ] 验证：为 admin 角色分配所有菜单，为 user 角色分配部分菜单

**检查点**：RBAC 的三层关联（用户↔角色↔菜单）全通了

---

### Phase 3：登录 + 权限守卫（Day 12-15）

#### Day 12：JWT 基础设施

- [ ] `JwtTokenProvider`（生成、校验、解析 token）
- [ ] 测试：生成一个 token，解析出 userId，校验过期

#### Day 13：登录接口

- [ ] `AuthController`：login / logout / me / changePassword
- [ ] `AuthService`：用户名密码校验、BCrypt 匹配
- [ ] 登录返回 `{token, expiresIn}`
- [ ] logout 时将 token 加入 Redis 黑名单（过期时间 = token 剩余有效期）

#### Day 14：拦截器

- [ ] `JwtAuthInterceptor`：解析 token → 校验黑名单 → 设置 UserContext
- [ ] `UserContext`：ThreadLocal 存当前用户信息
- [ ] `LoginUser`：存 userId、username、permissions 集合
- [ ] 在 `WebConfig` 注册拦截器，排除 `/api/auth/**` 路径

#### Day 15：权限校验

- [ ] `@RequirePermission` 注解
- [ ] 拦截器在设置 UserContext 时查询用户权限集合（带 Redis 缓存）
- [ ] 校验方法上的 `@RequirePermission`，无权限返回 403
- [ ] 验证：admin 能访问所有接口，user 访问用户删除接口返回 403

**检查点**：登录成功拿到 token，无 token 返回 401，无权限返回 403

---

### Phase 4：操作日志 + 数据字典（Day 16-19）

#### Day 16：操作日志 AOP

- [ ] `@SysLog` 注解
- [ ] `SysLogAspect` 切面
- [ ] 切面中获取当前用户、方法、参数、耗时
- [ ] 同步写库（先验证切面逻辑正确）

#### Day 17：操作日志异步改造

- [ ] `SysLogEntry` MQ 消息体
- [ ] RabbitMQ 配置：队列 `sys.log.queue`、交换机 `sys.log.exchange`
- [ ] 切面改为 `rabbitTemplate.convertAndSend()` 发送消息
- [ ] `SysLogConsumer` 消费者，收到消息后入库
- [ ] `SysLogService` + `SysLogController`：分页查询日志

#### Day 18：数据字典

- [ ] `DictType` + `DictItem` 实体
- [ ] `DictTypeMapper` + `DictItemMapper` + XML
- [ ] `DictService`（CRUD + 缓存）
- [ ] `DictTypeController` + `DictItemController`

#### Day 19：字典缓存

- [ ] `getItemsByTypeCode` 加 Redis 缓存
- [ ] 增删改时清除对应缓存
- [ ] 验证：第一次查字典走 DB，第二次走缓存

**检查点**：每次操作接口自动落日志，字典数据走 Redis 缓存

---

### Phase 5：收尾（Day 20-21）

#### Day 20：Docker Compose

- [ ] 写 `Dockerfile`（multi-stage build）
- [ ] 写 `docker-compose.yml`（MySQL + Redis + RabbitMQ + App 四个服务）
- [ ] 验证：`docker compose up` 一键启动全部服务

#### Day 21：README + 面试稿

- [ ] README：项目简介、技术栈、快速启动、功能列表、数据库 ER 图、Flyway 迁移说明
- [ ] 面试复盘稿：30 秒介绍、RBAC 模型解释、技术选型理由、10+ 追问 & 回答

**检查点**：另一个人能通过 README 把项目跑起来

---

## 6. 关键配置参考

### 6.1 pom.xml 关键依赖

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.13</version>
    <relativePath/>
</parent>

<properties>
    <java.version>21</java.version>
    <mybatis-spring-boot.version>3.0.5</mybatis-spring-boot.version>
    <pagehelper.version>4.1.0</pagehelper.version>
    <jjwt.version>0.13.0</jjwt.version>
    <knife4j.version>4.5.0</knife4j.version>
    <hutool.version>5.8.38</hutool.version>
</properties>

<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- MyBatis -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>${mybatis-spring-boot.version}</version>
    </dependency>

    <!-- MySQL -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- PageHelper -->
    <dependency>
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper-spring-boot-starter</artifactId>
        <version>${pagehelper.version}</version>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>${jjwt.version}</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>${jjwt.version}</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>${jjwt.version}</version>
        <scope>runtime</scope>
    </dependency>

    <!-- BCrypt -->
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-crypto</artifactId>
    </dependency>

    <!-- Knife4j -->
    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
        <version>${knife4j.version}</version>
    </dependency>

    <!-- Hutool -->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
        <version>${hutool.version}</version>
    </dependency>

    <!-- Database Migration -->
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-mysql</artifactId>
    </dependency>

    <!-- Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

如果希望源码在 Java 17 环境也能编译，把 `<java.version>` 改为 `17`，或在 Maven Compiler Plugin 中设置 `<release>17</release>`。当前本机 JDK 是 21，默认按 Java 21 开发即可。

### 6.2 application-dev.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/admin_system?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20

  data:
    redis:
      host: localhost
      port: 6379

  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.admin.module
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

pagehelper:
  helper-dialect: mysql
  reasonable: true
  support-methods-arguments: true

# Knife4j
springdoc:
  swagger-ui:
    path: /swagger-ui.html
knife4j:
  enable: true

# JWT
jwt:
  secret: your-base64-encoded-secret-at-least-256-bits
  expiration: 86400000

# 日志
logging:
  level:
    com.example.admin: debug
```

### 6.3 Docker Compose

```yaml
services:
  mysql:
    image: mysql:8.4
    container_name: admin-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: admin_system
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    command: --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci

  redis:
    image: redis:7-alpine
    container_name: admin-redis
    ports:
      - "6379:6379"

  rabbitmq:
    image: rabbitmq:3.13-management-alpine
    container_name: admin-rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: admin-app
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
      - rabbitmq
    environment:
      SPRING_PROFILES_ACTIVE: dev
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/admin_system?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=Asia/Shanghai
      SPRING_DATA_REDIS_HOST: redis
      SPRING_RABBITMQ_HOST: rabbitmq

volumes:
  mysql_data:
```

---

## 7. 测试矩阵

每个模块的 Service 需要覆盖：

| 测试场景 | 用户 | 角色 | 菜单 | 部门 |
|---------|:---:|:---:|:---:|:---:|
| 新增成功 | ✅ | ✅ | ✅ | ✅ |
| 新增-用户名/编码重复 | ✅ | ✅ | — | — |
| 删除-逻辑删除 | ✅ | — | — | — |
| 删除-有关联子节点拒绝 | — | — | ✅ | ✅ |
| 分页查询 | ✅ | ✅ | — | — |
| 模糊搜索 | ✅ | — | — | — |
| 树形组装 | — | — | ✅ | ✅ |
| 分配权限（事务原子性） | — | ✅ | — | — |
| 参数校验 | ✅ | ✅ | ✅ | ✅ |

认证模块：

| 测试场景 | 覆盖 |
|---------|:---:|
| 正确用户名密码 → 返回 token | ✅ |
| 错误密码 → 返回 401 | ✅ |
| 禁用用户 → 返回 403 | ✅ |
| 无 token → 返回 401 | ✅ |
| 过期 token → 返回 401 | ✅ |
| 无权限 → 返回 403 | ✅ |
| 登出后 token 不可用 | ✅ |

---

## 8. README 模板

````markdown
# Admin System - 企业后台管理系统

## 简介

基于 Spring Boot 3.5 + MyBatis + MySQL 8.4 的企业后台管理系统，实现 RBAC 权限模型、操作日志审计、数据字典缓存。

## 技术栈

| 层面 | 技术 |
|------|------|
| JDK | Java 21（可按需编译兼容 Java 17） |
| 构建工具 | Maven 3.9.x |
| 后端框架 | Spring Boot 3.5.x |
| ORM | MyBatis Spring Boot Starter 3.0.x |
| 数据库 | MySQL 8.4 LTS |
| 缓存 | Redis 7.x |
| 消息队列 | RabbitMQ 3.13.x |
| 认证 | JWT + BCrypt |
| 接口文档 | Knife4j |
| 部署 | Docker Compose |

## 功能模块

- [x] 用户管理（CRUD / 分页 / 模糊搜索 / 状态管理）
- [x] 角色管理（CRUD / 分配菜单权限）
- [x] 菜单管理（三级树形：目录 → 菜单 → 按钮）
- [x] 部门管理（树形结构）
- [x] 登录认证（JWT / 密码加密 / 权限校验）
- [x] 操作日志（AOP + MQ 异步写入）
- [x] 数据字典（Redis 缓存）

## 快速启动

### 1. Docker Compose（推荐）

```bash
docker compose up -d
```

启动后访问：
- API 接口：`http://localhost:8080`
- 接口文档：`http://localhost:8080/doc.html`

默认账号：`admin` / `admin123`

### 2. 手动启动

1. 创建数据库 `admin_system`
2. 执行 Flyway 迁移，或手工执行 `docs/schema.sql` 建表
3. 修改 `application-dev.yml` 中的数据库连接信息
4. 启动应用：`mvn spring-boot:run`

## 数据库设计

用户 ──< 用户角色关联 >── 角色 ──< 角色菜单关联 >── 菜单
 │
 └── 部门

## 权限模型（RBAC）

user → user_role → role → role_menu → menu.permission

权限校验流程：
1. 用户登录 → 生成 JWT
2. 后续请求携带 Authorization header
3. 拦截器解析 token → 查询用户权限集合（Redis 缓存）→ 匹配接口权限

## 项目结构

```
com.example.admin
├── common/         统一响应、异常
├── config/         跨域、MyBatis、Redis、RabbitMQ 配置
├── security/       JWT、拦截器、权限校验
├── module/
│   ├── auth/       登录/登出
│   ├── user/       用户管理
│   ├── role/       角色管理
│   ├── menu/       菜单管理
│   ├── dept/       部门管理
│   ├── log/        操作日志
│   └── dict/       数据字典
└── resources/      配置 + MyBatis XML
```
````

---

## 9. 面试复盘稿

### 9.1 30 秒介绍

> "这是一个标准的企业后台管理系统，基于 Spring Boot + MyBatis + MySQL，实现了 RBAC 权限模型。核心功能包括用户管理、角色管理、菜单权限树、部门树、JWT 登录认证、操作日志的 AOP 异步记录、以及数据字典的 Redis 缓存。权限校验我从 JWT 解析到权限集合缓存都自己写了一遍，没有用 Spring Security。整个系统用 Docker Compose 一键启动。"

### 9.2 RBAC 模型 —— 面试必问

> "RBAC 是 Role-Based Access Control 的缩写。核心思想是：不直接把权限赋给用户，而是通过角色作为中间层。用户拥有角色，角色拥有菜单权限。权限的粒度是菜单上的 `permission` 字段，比如 `sys:user:add`。当用户登录时，我从 user 表出发，通过 user_role 关联表找到用户的角色，再通过 role_menu 关联表找到所有菜单的 permission 标识符，组成一个权限集合。之后每次请求到达时，拦截器检查当前接口需要的权限是否在这个集合中。"

### 9.3 10 个高概率追问

| 追问 | 回答要点 |
|------|---------|
| **为什么不直接用 Spring Security？** | 太重，配置链复杂。手写 JWT 拦截器让我理解每一步——token 解析、黑名单校验、权限匹配。而且面试能讲清楚每个环节 |
| **JWT 怎么实现登出？** | JWT 本身是无状态的，我通过 Redis 维护了一个黑名单。登出时把 token 加入黑名单，过期时间设为 token 的剩余有效期。拦截器在解析 token 之后先查黑名单 |
| **权限集合为什么缓存到 Redis？** | 每次请求都去查三张关联表（user_role + role_menu + menu）太慢。登录时查一次，写入 Redis，设置合理过期时间。角色变更时主动删除缓存 |
| **菜单树怎么组装的？** | 先把所有菜单查出来（一条 SQL），然后在 Java 层递归组装：遍历找 parent_id=0 的作为根节点，对每个根节点递归找自己的子节点 |
| **操作日志为什么用 MQ 异步？** | 日志不是业务主流程，同步写库会增加接口响应时间。通过 RabbitMQ 异步写入，切面里只是发一条消息，不阻塞请求 |
| **数据字典缓存的更新策略？** | Cache Aside 模式：读的时候先查缓存，缓存没有查 DB 并回写缓存；写的时候先更新 DB，再删除缓存。不直接更新缓存是因为并发情况下会有数据不一致 |
| **MyBatis 分页怎么做的？** | 用 PageHelper 插件。调用 `PageHelper.startPage(page, size)` 后，紧接着的 SQL 查询会被拦截，自动加上 LIMIT 并发出 COUNT 查询 |
| **逻辑删除怎么处理？** | `deleted` 字段标记，所有查询带 `WHERE deleted=0`，不是真正 DELETE。这是数据安全的基本实践——删错了还能恢复 |
| **ThreadLocal 存用户信息有什么注意事项？** | 请求结束后必须 `remove()`。Tomcat 线程池会复用线程，如果不清理，下一次请求可能读到上一个用户的信息——这是个严重的安全问题 |
| **如果让你加一个功能，你会做什么？** | 数据权限——当前实现了菜单权限（能不能访问这个接口），但没有数据权限（能访问这个接口但只能看到自己部门的数据）。实现方式可以给 SQL 加动态过滤条件，或者在业务层根据用户的部门 ID 过滤 |

---

## 10. 验收标准

### 功能验收

- [ ] 用户增删改查 + 分页 + 模糊搜索 + 状态切换
- [ ] 角色增删改查 + 分配菜单权限
- [ ] 菜单树正确返回三级结构（目录/菜单/按钮）
- [ ] 部门树正确返回
- [ ] 登录返回 JWT，过期后拒绝
- [ ] 无权限返回 403
- [ ] 操作日志自动记录（任何带 `@SysLog` 的接口）
- [ ] 字典数据走 Redis 缓存
- [ ] Knife4j 接口文档可访问 `http://localhost:8080/doc.html`

### 工程质量验收

- [ ] 统一响应格式 `{code, message, data}`
- [ ] 参数校验返回中文错误提示
- [ ] 全局异常捕获，不暴露堆栈给前端
- [ ] 所有 Service 至少有一个单元测试
- [ ] Docker Compose 一键启动全栈

### 文档验收

- [ ] README 有架构说明、快速启动、功能列表
- [ ] 建表 SQL 放在 `docs/schema.sql`
- [ ] 面试复盘稿在 `docs/INTERVIEW.md`
