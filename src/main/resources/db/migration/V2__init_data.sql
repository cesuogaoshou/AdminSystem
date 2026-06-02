INSERT INTO sys_dept (id, parent_id, name, leader, phone, sort_order, status) VALUES
(1, 0, '总公司', 'CEO', '10000000000', 1, 1),
(2, 1, '技术部', 'CTO', '10000000001', 2, 1),
(3, 1, '产品部', 'CPO', '10000000002', 3, 1);

INSERT INTO sys_role (id, name, code, description, status, sort_order) VALUES
(1, '超级管理员', 'admin', '拥有系统全部权限', 1, 1),
(2, '普通用户', 'user', '拥有基础查看权限', 1, 2);

INSERT INTO sys_user (
    id, username, password, nickname, email, phone, gender,
    dept_id, status, create_by, update_by, deleted
) VALUES (
    1,
    'admin',
    '$2a$10$DowJones7V8mShR7Rz9GjQe6fZugpU47G7r4sCU8hdVZZkwYArdOqm',
    '超级管理员',
    'admin@example.com',
    '10000000003',
    0,
    2,
    1,
    'system',
    'system',
    0
);

INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1);

INSERT INTO sys_menu (
    id, parent_id, name, type, path, component, permission, icon, sort_order, visible
) VALUES
(1, 0, '系统管理', 1, '/system', NULL, NULL, 'setting', 1, 1),

(2, 1, '用户管理', 2, '/system/users', 'system/user/index', 'sys:user:list', 'user', 1, 1),
(3, 2, '用户新增', 3, NULL, NULL, 'sys:user:add', NULL, 1, 1),
(4, 2, '用户修改', 3, NULL, NULL, 'sys:user:update', NULL, 2, 1),
(5, 2, '用户删除', 3, NULL, NULL, 'sys:user:delete', NULL, 3, 1),

(6, 1, '角色管理', 2, '/system/roles', 'system/role/index', 'sys:role:list', 'role', 2, 1),
(7, 6, '角色新增', 3, NULL, NULL, 'sys:role:add', NULL, 1, 1),
(8, 6, '角色修改', 3, NULL, NULL, 'sys:role:update', NULL, 2, 1),
(9, 6, '角色删除', 3, NULL, NULL, 'sys:role:delete', NULL, 3, 1),

(10, 1, '菜单管理', 2, '/system/menus', 'system/menu/index', 'sys:menu:list', 'menu', 3, 1),
(11, 10, '菜单新增', 3, NULL, NULL, 'sys:menu:add', NULL, 1, 1),
(12, 10, '菜单修改', 3, NULL, NULL, 'sys:menu:update', NULL, 2, 1),
(13, 10, '菜单删除', 3, NULL, NULL, 'sys:menu:delete', NULL, 3, 1),

(14, 1, '部门管理', 2, '/system/depts', 'system/dept/index', 'sys:dept:list', 'dept', 4, 1),
(15, 14, '部门新增', 3, NULL, NULL, 'sys:dept:add', NULL, 1, 1),
(16, 14, '部门修改', 3, NULL, NULL, 'sys:dept:update', NULL, 2, 1),
(17, 14, '部门删除', 3, NULL, NULL, 'sys:dept:delete', NULL, 3, 1);

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu;