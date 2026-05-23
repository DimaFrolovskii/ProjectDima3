# ЭТАП 1: Сборка проекта (используем тяжелый образ с установленным Maven и JDK)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Копируем конфигурацию Maven и исходный код
COPY pom.xml .
COPY src ./src
# Запускаем сборку .jar файла, пропуская тесты для скорости
RUN mvn clean package -DskipTests

# ЭТАП 2: Создание финального образа (используем легкий образ только с JRE)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Копируем собранный .jar файл из первого этапа (build)
COPY --from=build /app/target/*.jar app.jar
# Сообщаем Docker, что приложение слушает порт 8080
EXPOSE 8080
# Команда запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]