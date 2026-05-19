# Etapa 1: Construcción del JAR (Se mantiene igual)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final ligera con solo el JAR (¡Aquí cambias la línea 9!)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9595

ENTRYPOINT ["java", "-jar", "app.jar"]
