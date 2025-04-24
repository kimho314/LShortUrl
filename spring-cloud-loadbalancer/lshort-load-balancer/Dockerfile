FROM amazoncorretto:21 AS builder
WORKDIR /spring-cloud-loadbalancer
COPY . .
RUN ./gradlew clean build -x test

FROM amazoncorretto:21
WORKDIR /spring-cloud-loadbalancer
COPY --from=builder /spring-cloud-loadbalancer/build/libs/*.jar load-balancer.jar
CMD ["java", "-Xms512m", "-Xmx512m", "-jar", "load-balancer.jar"]