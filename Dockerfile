# Build Jar
FROM gradle:jdk21-alpine AS builder
WORKDIR /app
COPY . /app
RUN gradle build --no-daemon

ENTRYPOINT [ "java" , "-jar" , "/app/build/libs/tartarus.jar" ]