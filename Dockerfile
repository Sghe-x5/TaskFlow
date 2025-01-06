FROM openjdk:17-jdk-slim

WORKDIR /app

# Скопировать скрипт Maven Wrapper
COPY mvnw .
COPY .mvn .mvn

# Скопировать проект
COPY pom.xml .
COPY src ./src

# Убедиться, что mvnw исполняемый
RUN chmod +x mvnw

# Сборка проекта
RUN ./mvnw package -DskipTests

# Указать точку входа
CMD ["java", "-jar", "target/TaskFlow-1.0.0-SNAPSHOT.jar"]
