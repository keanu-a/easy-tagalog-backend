# Use Maven to build the JAR
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy source code
COPY pom.xml .
COPY src ./src

# Build the app (skips tests to speed up deploy)
RUN mvn clean package -DskipTests

# Use a minimal JRE to run the app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]