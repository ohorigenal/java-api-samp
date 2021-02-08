FROM adoptopenjdk/openjdk11:alpine-slim as builder

WORKDIR /project

COPY . .

RUN ./gradlew bootJar

FROM adoptopenjdk/openjdk11:alpine-slim

WORKDIR /project

COPY --from=builder /project/build/libs/java-api-samp.jar .

ENTRYPOINT ["java", "-jar", "java-api-samp.jar"]
