# FROM openjdk:17-jdk-alpine
# ADD build/libs/*.jar dockerapp.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","dockerapp.jar"]
# Stage 1: Build the app
FROM gradle:7.5-jdk17-alpine AS builder
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle clean build -x test

# Stage 2: Runtime image
FROM openjdk:17-jdk-alpine
WORKDIR /app
EXPOSE 8080

# Copy the JAR from the builder
COPY --from=builder /home/gradle/src/build/libs/*.jar dockerapp.jar

# Use urandom for faster startup and lower disk usage
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "dockerapp.jar"]
