CREATE TABLE sys_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) COMMENT '操作用户',
    module VARCHAR(100) COMMENT '操作模块',
    operation VARCHAR(100) COMMENT '操作描述',
    method VARCHAR(200) COMMENT '请求方法',
    request_url VARCHAR(200) COMMENT '请求URL',
    request_ip VARCHAR(50) COMMENT '请求IP',
    params TEXT COMMENT '请求参数(JSON)',
    result TEXT COMMENT '返回结果(JSON)',
    duration BIGINT COMMENT '耗时(ms)',
    status TINYINT DEFAULT 1 COMMENT '1成功 0失败',
    error_msg TEXT COMMENT '异常信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    KEY idx_sys_log_username (username),
    KEY idx_sys_log_create_time (create_time),
    KEY idx_sys_log_module (module)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

CREATE TABLE sys_dict_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '字典名称',
    code VARCHAR(50) NOT NULL COMMENT '字典编码',
    description VARCHAR(200) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '1启用 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_sys_dict_type_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

CREATE TABLE sys_dict_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_id BIGINT NOT NULL COMMENT '字典类型ID',
    label VARCHAR(50) NOT NULL COMMENT '字典标签',
    value VARCHAR(50) NOT NULL COMMENT '字典值',
    color VARCHAR(20) COMMENT '标签颜色',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '1启用 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_sys_dict_item_type (type_id),
    UNIQUE KEY uk_sys_dict_item_type_value (type_id, value)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典项表';

INSERT INTO sys_dict_type (id, name, code, description, status) VALUES
(1, '用户性别', 'gender', '用户性别字典', 1),
(2, '通用状态', 'status', '启用禁用状态字典', 1);

INSERT INTO sys_dict_item (type_id, label, value, color, sort_order, status) VALUES
(1, '未知', '0', 'gray', 0, 1),
(1, '男', '1', 'blue', 1, 1),
(1, '女', '2', 'pink', 2, 1),
(2, '禁用', '0', 'red', 0, 1),
(2, '启用', '1', 'green', 1, 1);