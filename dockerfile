FROM openjdk:11-jdk-slim
WORKDIR /usr/src/app
COPY target/CarNotify-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/src/app
ENTRYPOINT ["java", "-jar", "/usr/src/app/CarNotify-1.0-SNAPSHOT-jar-with-dependencies.jar"]
