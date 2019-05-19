FROM openjdk:8-jdk-alpine
VOLUME /tmp
# make sure the jar file is located in the current directory
COPY Xfactory-0.0.1.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]