# 🌟 ProjectDima3 🌟
## :bangbang: Мой проект по учебе на 3м курсе :bangbang: 
## цифровая система для защиты и учета важного оборудования на государственных или промышленных объектах ##


### 🛠 Стек
### Backend: ### 
Java 17, Spring Boot 3.x (Spring Web, Spring Data JPA, Validation).

### Security: ###
Spring Security, JWT (JSON Web Token).

### Database: ### 
PostgreSQL, PostgreSQL Driver.

### Migrations: ### 
Flyway.

### Utilities: ### 
Lombok.

### Testing: ###
JUnit 5, Mockito, Spring Boot Test, Spring Security Test, WebMvcTest.

### DevOps: ###
Docker & Docker Compose.


### 🚀 Функционал
Учет активов (Assets): Полный CRUD цикл управления оборудованием с привязкой к локациям.

Иерархия данных: Связи между объектами (Facilities), активами и журналом происшествий (Incidents).

Безопасность: Авторизация на основе JWT (JSON Web Tokens) с разделением ролей:

USER — просмотр и редактирование данных.

ADMIN — полный доступ, включая безвозвратное удаление.

Пагинация и поиск: Оптимизированный вывод данных через Pageable и гибкие фильтры в репозиториях.

Надежность: Глобальная обработка исключений и валидация входящих данных.


### Структура проекта 
```
ProjectDima3/
├── src/
│   ├── main/
│   │   ├── java/com/example/ProjectDima3/
│   │   │   ├── config/          # Конфигурации приложения (Swagger, CORS, Bean-компоненты)
│   │   │   ├── controller/      # REST-контроллеры (обработка HTTP-запросов)
│   │   │   ├── dto/             # Data Transfer Objects (объекты для передачи данных)
│   │   │   ├── entity/          # Сущности базы данных (JPA/Hibernate)
│   │   │   ├── exception/       # Глобальная обработка ошибок и кастомные исключения
│   │   │   ├── repository/      # Интерфейсы для работы с БД (Spring Data JPA)
│   │   │   ├── security/        # Настройки безопасности (JWT фильтры и утилиты)
│   │   │   │   ├── JwtFilter.java
│   │   │   │   └── JwtUtil.java
│   │   │   ├── service/         # Бизнес-логика приложения
│   │   │   └── ProjectDima3Application.java # Точка входа в приложение
│   │   └── resources/
│   │       └── application.properties       # Главный файл настроек Spring Boot
│   └── test/                    # Модульные и интеграционные тесты
├── .gitignore                   # Исключения для систем контроля версий
├── Dockerfile                   # Инструкции для сборки Docker-образа
├── docker-compose.yml           # Оркестрация контейнеров (БД, приложение и др.)
├── pom.xml                      # Конфигурация зависимостей Maven
└── README.md                    # Документация проекта
```



### 📦 Запуск
1.Соберите проект: mvn clean package

2.Запустите контейнеры: docker-compose up --build

3.API будет доступно по адресу: http://localhost:8080


### Автор 

## Фроловский Дмитрий Сергеевич

Проект сделан для финальной работе по Java
