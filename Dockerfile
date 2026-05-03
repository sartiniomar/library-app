# ===== BUILD STAGE =====
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copiamos primero pom.xml para cachear dependencias
COPY pom.xml .
RUN mvn -B -q -e -DskipTests dependency:go-offline

# Ahora sí copiamos el resto del código
COPY src ./src

# Build
RUN mvn -B -q clean package -DskipTests

# ===== RUNTIME STAGE =====
FROM eclipse-temurin:21-jdk-jammy

# Instalar curl (solo lo necesario)
RUN apt-get update \
    && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar solo el jar final
COPY --from=builder /build/target/app.jar app.jar

# Seguridad: no correr como root
RUN useradd -ms /bin/bash appuser
USER appuser

EXPOSE 8080

# Healthcheck (ya validado 👊)
HEALTHCHECK --interval=30s --timeout=5s --retries=3 --start-period=20s \
CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","app.jar"]