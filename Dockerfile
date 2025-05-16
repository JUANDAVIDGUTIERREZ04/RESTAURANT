# Etapa 1: Construcción del JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final ligera con solo el JAR
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Copiar archivos estáticos (opcional)
COPY src/main/resources/static /app/static

EXPOSE 9292

ENTRYPOINT ["java", "-jar", "app.jar"]
