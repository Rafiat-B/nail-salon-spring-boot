# Use a base image with Java 17
FROM eclipse-temurin:17-jdk-alpine

# Create app directory
WORKDIR /app

# Copy the JAR file into the image
COPY target/nail-salon-app.jar app.jar
COPY src/main/resources/templates /templates/

# Expose default port (override using profile-specific ports)
EXPOSE 9090

# Add your environment variables to the ENTRYPOINT command
ENTRYPOINT ["java", "-jar", "app.jar", \
            "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
            "--spring.datasource.url=${SPRING_DATASOURCE_URL}", \
            "--spring.datasource.username=${SPRING_DATASOURCE_USERNAME}", \
            "--spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}"]
