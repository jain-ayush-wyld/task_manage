# Stage 1: Use Gradle with JDK 17 (non-Alpine for compatibility)
FROM gradle:8.2-jdk17 AS builder

WORKDIR /app

# Copy project files
COPY . .

# Use Gradle wrapper to ensure correct version
RUN chmod +x ./gradlew \
    && ./gradlew clean build -x test

# Stage 2: Runtime image with lightweight JDK
FROM openjdk:17-jdk-slim

WORKDIR /app
EXPOSE 8080

# Copy fat jar from builder
COPY --from=builder /app/build/libs/*.jar dockerapp.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "dockerapp.jar"]
