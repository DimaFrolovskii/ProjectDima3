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

C:/ProjectDima3
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── ProjectDima3
│   │   │               ├── config
│   │   │               ├── controller
│   │   │               ├── dto
│   │   │               ├── entity
│   │   │               ├── exception
│   │   │               ├── repository
│   │   │               ├── security
│   │   │               │   ├── JwtFilter.java
│   │   │               │   └── JwtUtil.java
│   │   │               ├── service
│   │   │               └── ProjectDima3Application.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── ProjectDima3
│                       └── ProjectDima3ApplicationTests.java
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md





### 📦 Запуск
1.Соберите проект: mvn clean package

2.Запустите контейнеры: docker-compose up --build

3.API будет доступно по адресу: http://localhost:8080


### Автор 

## Фроловский Дмитрий Сергеевич

Проект сделан для финальной работе по Java
