# 빌드 스테이지
FROM openjdk:21-jdk-slim AS build
WORKDIR /app
COPY . /app
RUN chmod +x gradlew && ./gradlew clean build
RUN ls -l /app/build/libs/

# 실행 스테이지
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/shop-0.0.1-SNAPSHOT.jar /app/shop.jar
RUN chmod +x /app/shop.jar
RUN ls -l /app
CMD ["java", "-jar", "/app/shop.jar"]
