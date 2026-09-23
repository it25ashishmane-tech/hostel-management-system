FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/hostel-management-system-1.0.jar app.jar

EXPOSE 8081

CMD ["java", "-cp", "app.jar", "com.example.App"]