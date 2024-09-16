# Build Jar
FROM gradle:jdk21-alpine AS builder
WORKDIR /app
COPY . /app
RUN gradle build --no-daemon

# Runtime image
FROM openjdk:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/tartarus.jar /app/tartarus.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/tartarus.jar"]
