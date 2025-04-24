FROM amazoncorretto:21 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

FROM amazoncorretto:21
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
CMD ["java", "-Xms1024m", "-Xmx1024m", "-jar", "app.jar"]