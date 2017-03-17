FROM java:8

MAINTAINER spike19820318@gmail.com

COPY build/libs/*.jar /opt/

CMD ["java", "-jar", "opt/ps-authservice-0.0.1-SNAPSHOT.jar"]
