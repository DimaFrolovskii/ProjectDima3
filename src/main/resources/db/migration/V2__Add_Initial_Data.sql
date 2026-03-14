-- Добавляем тестового админа (пароль 123456)
INSERT INTO users (username, password, email)
VALUES ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8q6uy5clBRs3pX57Y75U876L2pYmBsn7G6W', 'admin@example.com')
ON CONFLICT (username) DO NOTHING;

-- Добавляем стартовые активы
INSERT INTO assets (name, type, status)
VALUES
('MacBook Pro 16', 'Hardware', 'Active'),
('Лицензия Adobe CC', 'Software', 'Active'),
('Монитор LG 27', 'Hardware', 'In Stock');