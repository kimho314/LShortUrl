FROM amazoncorretto:21
WORKDIR /app
COPY . /app
RUN chmod +x ./gradlew && ./gradlew build -x test
CMD ["java", "-Xms256m", "-Xmx256m", "-jar", "build/libs/lshort-load-balancer-0.0.1-SNAPSHOT.jar"]