FROM openjdk:21-jdk-slim

WORKDIR /app
COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build.gradle.kts /app/build.gradle.kts
COPY settings.gradle.kts /app/settings.gradle.kts
COPY src /app/src

RUN chmod +x gradlew && ./gradlew clean build && ls -l build/libs/

COPY --from=build /app/build/libs/*.jar /app/shop.jar

CMD ["java", "-jar", "/app/shop.jar"]
