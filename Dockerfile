FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle .
COPY src ./src
RUN chmod +x gradlew \
    && ./gradlew --no-daemon bootJar -x test \
    && find build/libs -type f -name '*.jar' ! -name '*-plain.jar' -exec cp {} /tmp/app.jar \; \
    && test -s /tmp/app.jar

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /tmp/app.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=render
ENTRYPOINT ["java", "-jar", "app.jar"]
