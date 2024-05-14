FROM gradle:8.7-jdk17 as build

WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src /app/src

RUN chmod +x ./gradlew
RUN ./gradlew bootJar -x test



FROM openjdk:17-alpine
COPY --from=build /app/build/libs/*.jar /app/backend.jar

CMD ["java", "-Xmx4g", "-jar", "/app/backend.jar"]