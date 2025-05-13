# Stage 1: Build the app using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app using a minimal JDK image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy only the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
COPY src/main/resources/templates /templates/

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar", \
            "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
            "--spring.datasource.url=${SPRING_DATASOURCE_URL}", \
            "--spring.datasource.username=${SPRING_DATASOURCE_USERNAME}", \
            "--spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}"]
