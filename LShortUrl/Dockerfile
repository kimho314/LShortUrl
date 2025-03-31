FROM amazoncorretto:21
WORKDIR /app
COPY . /app
RUN chmod +x ./gradlew && ./gradlew build -x test
CMD ["java", "-jar", "build/libs/LShortUrl-0.0.1-SNAPSHOT.jar"]