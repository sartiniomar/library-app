# Usar imagen base ligera con Java 21
FROM eclipse-temurin:21-jdk-jammy

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el jar generado
COPY target/app.jar app.jar

# Exponer puerto
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java","-jar","app.jar"]

RUN apt-get update && apt-get install -y curl

HEALTHCHECK --interval=30s --timeout=5s --retries=3 --start-period=20s \
CMD curl -f http://localhost:8080/actuator/health || exit 1