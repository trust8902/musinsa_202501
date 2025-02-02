FROM openjdk:21-jdk-slim

WORKDIR /app
COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build.gradle.kts /app/build.gradle.kts
COPY settings.gradle.kts /app/settings.gradle.kts
COPY src /app/src

RUN chmod +x gradlew && ./gradlew clean build

RUN echo "Built files in build/libs/:" && ls -l build/libs/

COPY build/libs/shop-0.0.1-SNAPSHOT.jar /app/shop.jar

CMD ["java", "-jar", "/app/shop.jar"]
