FROM openjdk:21
VOLUME /tmp
COPY build/libs/tartarus-0.0.1-SNAPSHOT.jar tartarus.jar
ENTRYPOINT [ "java" , "-jar" , "/tartarus.jar" ]