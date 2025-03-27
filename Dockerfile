#
# Build stage
#
FROM maven:3.8.2-jdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /app
COPY --from=build target/quasar-service-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
