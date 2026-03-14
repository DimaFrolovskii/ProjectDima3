-- 1. Добавляем тестовых пользователей
-- Пароль для всех: 'password' (захеширован через BCrypt, как того требует Spring Security)
INSERT INTO users (username, password, email)
VALUES
('admin', '$2a$10$8.UnVuG9HHgffUDAlk8q6uy5clBRs3pX57Y75U876L2pYmBsn7G6W', 'admin@example.com'),
('user1', '$2a$10$8.UnVuG9HHgffUDAlk8q6uy5clBRs3pX57Y75U876L2pYmBsn7G6W', 'user1@example.com')
ON CONFLICT (username) DO NOTHING;

-- 2. Добавляем начальные активы (Assets) для отображения в таблице
INSERT INTO assets (name, type, status)
VALUES
('Ноутбук Lenovo Legion', 'Hardware', 'Active'),
('Лицензия IntelliJ IDEA', 'Software', 'Active'),
('Принтер HP LaserJet', 'Hardware', 'Maintenance'),
('Подписка Adobe Creative Cloud', 'Software', 'Expired'),
('Сервер Dell PowerEdge', 'Hardware', 'Active');