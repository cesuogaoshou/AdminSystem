UPDATE sys_user
SET password = '$2a$10$Gc8vk0F8iLJKD6ZQ65HEr.uTUw3vjFJaQ/DEXafRUoreDo3Y0dxGq',
    update_by = 'system',
    update_time = NOW()
WHERE username = 'admin'
  AND deleted = 0;
