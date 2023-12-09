FROM openjdk:8-jdk-alpine

EXPOSE 8083

#COPY target/eventsProject-0.0.1-SNAPSHOT.jar  eventsProject-0.0.1-SNAPSHOT.jar
#ADD target/DevOps_Project-0.0.1-SNAPSHOT.jar DevOps_Project-0.0.1-SNAPSHOT.jar
ADD http://192.168.1.62:8081/repository/maven-releases/tn/esprit/eventsProject/0.0.1/eventsProject-0.0.1-SNAPSHOT.jar eventsProject-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/eventsProject-0.0.1-SNAPSHOT.jar"]