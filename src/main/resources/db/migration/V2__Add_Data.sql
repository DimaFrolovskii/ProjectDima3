-- Этот файл содержит начальные данные для заполнения базы данных

-- Добавление пользователей
-- Пароль для admin: 'admin'
-- Пароль для manager: 'password'
-- Пароль для user: 'password'
INSERT INTO users (username, password, email, role) VALUES
('admin', '$2a$10$f8b.jR9S8T0.l3.x1b.QieoB.VFFgqY.i.g.C/z.Z.C.C/z.Z.C', 'admin@example.com', 'ROLE_ADMIN'),
('manager', '$2a$10$8.UnVuG9HHgffUDAlk8q6uy5clBRs3pX57Y75U876L2pYmBsn7G6W', 'manager@example.com', 'ROLE_MANAGER'),
('user', '$2a$10$8.UnVuG9HHgffUDAlk8q6uy5clBRs3pX57Y75U876L2pYmBsn7G6W', 'user@example.com', 'ROLE_USER');

-- Добавление компаний
INSERT INTO companies (name, address) VALUES
('Tech Solutions Inc.', '123 Tech Street, Silicon Valley'),
('Green Innovations Ltd.', '456 Eco Avenue, Green City');

-- Добавление отделов
INSERT INTO departments (name, company_id) VALUES
('Разработка', 1),
('Маркетинг', 1),
('Исследования', 2);

-- Привязка пользователей к компаниям и отделам
UPDATE users SET company_id = 1, department_id = 1 WHERE username = 'manager';
UPDATE users SET company_id = 1, department_id = 2 WHERE username = 'user';
