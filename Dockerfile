# Build Jar
FROM gradle:jdk21-alpine AS builder
WORKDIR /app
COPY . /app
RUN gradle build --no-daemon

ENTRYPOINT [ "java" , "-jar" , "/app/build/libs/tartarus.jar" ]

# Create the Tartarus Image
#FROM openjdk:21
#VOLUME /tmp
#WORKDIR /app
#COPY --from=builder /app/build/libs/tartarus.jar /app/tartarus.jar
#ENTRYPOINT [ "java" , "-jar" , "/app/tartarus.jar" ]