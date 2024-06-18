# Build Jar
FROM gradle:jdk21-alpine AS build
VOLUME /tmp
COPY . .
RUN gradle build --no-daemon

# Create the Tartarus Image
FROM openjdk:21
VOLUME /tmp
COPY build/libs/tartarus-0.0.1-SNAPSHOT.jar tartarus.jar
ENTRYPOINT [ "java" , "-jar" , "/tartarus.jar" ]