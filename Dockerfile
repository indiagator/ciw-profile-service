FROM alpine:3.18 AS base

# Install OpenJDK 17
RUN apk add --no-cache openjdk17

LABEL authors="indiagator"

# Copy the JAR file into the container
COPY target/profile-service-dev-1.jar app.jar

# Expose the port the app runs on
EXPOSE 80:80

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]