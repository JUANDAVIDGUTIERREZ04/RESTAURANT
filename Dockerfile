# Imagen base con Java 21
FROM openjdk:21-slim

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR al contenedor
COPY target/*.jar app.jar


# Copiar archivos estáticos si los necesitas en tiempo de ejecución
COPY src/main/resources/static /app/static

# Exponer el puerto de la aplicación
EXPOSE 9292

# Ejecutar el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
