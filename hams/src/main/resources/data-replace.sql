-- Synthetic LOCAL DEMO data only. Never run against production.
-- Existing table schema is required; this script does not erase tables.
INSERT INTO tb_group_data (id, parent, name, usable, created_date, modified_date) VALUES
(1, NULL, 'Example group 1', 'Y', NOW(), NOW()),
(2, NULL, 'Example group 2', 'Y', NOW(), NOW()),
(3, NULL, 'Example group 3', 'Y', NOW(), NOW()),
(4, NULL, 'Example group 4', 'Y', NOW(), NOW()),
(5, NULL, 'Example group 5', 'Y', NOW(), NOW()),
(6, NULL, 'Example group 6', 'Y', NOW(), NOW());
INSERT INTO tb_equipment_data (id, name, latitude, longitude, ip, port, jibun_address, roadname_address, extra_address, group_id, usable, setup_date, modified_date) VALUES
(1, 'Example facility 1', 0.0, 0.0, '203.0.113.1', 3000, 'Example address', NULL, NULL, 1, 'Y', NOW(), NOW()),
(2, 'Example facility 2', 0.0, 0.0, '203.0.113.2', 3000, 'Example address', NULL, NULL, 2, 'Y', NOW(), NOW()),
(3, 'Example facility 3', 0.0, 0.0, '203.0.113.3', 3000, 'Example address', NULL, NULL, 3, 'Y', NOW(), NOW()),
(4, 'Example facility 4', 0.0, 0.0, '203.0.113.4', 3000, 'Example address', NULL, NULL, 4, 'Y', NOW(), NOW()),
(5, 'Example facility 5', 0.0, 0.0, '203.0.113.5', 3000, 'Example address', NULL, NULL, 5, 'Y', NOW(), NOW()),
(6, 'Example facility 6', 0.0, 0.0, '203.0.113.6', 3000, 'Example address', NULL, NULL, 6, 'Y', NOW(), NOW());
