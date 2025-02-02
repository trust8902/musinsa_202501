FROM openjdk:21-jdk-slim

COPY build/libs/shop-0.0.1-SNAPSHOT.jar /app/shop.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "shop.jar"]
