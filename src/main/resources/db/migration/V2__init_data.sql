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
    '$2a$10$Gc8vk0F8iLJKD6ZQ65HEr.uTUw3vjFJaQ/DEXafRUoreDo3Y0dxGq',
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
(17, 14, '部门删除', 3, NULL, NULL, 'sys:dept:delete', NULL, 3, 1),

(18, 1, '字典管理', 2, '/system/dicts', 'system/dict/index', 'sys:dict:list', 'dict', 5, 1),
(19, 18, '字典新增', 3, NULL, NULL, 'sys:dict:add', NULL, 1, 1),
(20, 18, '字典修改', 3, NULL, NULL, 'sys:dict:update', NULL, 2, 1),
(21, 18, '字典删除', 3, NULL, NULL, 'sys:dict:delete', NULL, 3, 1),

(22, 1, '操作日志', 2, '/system/logs', 'system/log/index', 'sys:log:list', 'log', 6, 1);

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu;

INSERT INTO sys_dict_type (id, name, code, description, status) VALUES
(1, '用户性别', 'gender', '用户性别字典', 1),
(2, '通用状态', 'status', '启用禁用状态字典', 1);

INSERT INTO sys_dict_item (type_id, label, value, color, sort_order, status) VALUES
(1, '未知', '0', 'gray', 0, 1),
(1, '男', '1', 'blue', 1, 1),
(1, '女', '2', 'pink', 2, 1),
(2, '禁用', '0', 'red', 0, 1),
(2, '启用', '1', 'green', 1, 1);
