FROM openjdk:17
WORKDIR /app
COPY --from=target/quasar-service-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
