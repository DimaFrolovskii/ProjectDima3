INSERT INTO users (username, password, email, role)
VALUES
('admin', '$2a$10$8.UnVuG9HHgffUDAlk8q6uy5clBRs3pX57Y75U876L2pYmBsn7G6W', 'admin@example.com', 'ROLE_ADMIN'),
('user1', '$2a$10$8.UnVuG9HHgffUDAlk8q6uy5clBRs3pX57Y75U876L2pYmBsn7G6W', 'user1@example.com', 'ROLE_USER')
ON CONFLICT (username) DO UPDATE SET role = EXCLUDED.role;

-- 2. Добавляем начальные активы (Assets) для отображения в таблице
INSERT INTO assets (name, type, status)
VALUES
('Ноутбук Lenovo Legion', 'Hardware', 'Active'),
('Лицензия IntelliJ IDEA', 'Software', 'Active'),
('Принтер HP LaserJet', 'Hardware', 'Maintenance'),
('Подписка Adobe Creative Cloud', 'Software', 'Expired'),
('Сервер Dell PowerEdge', 'Hardware', 'Active');