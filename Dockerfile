# 빌드 스테이지
FROM openjdk:21-jdk-slim AS build
WORKDIR /app
COPY . /app
RUN chmod +x gradlew && ./gradlew clean build

# 실행 스테이지
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/shop.jar
CMD ["java", "-jar", "/app/shop.jar"]
