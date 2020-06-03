#pull official docker image for ReactJS
FROM maven:3.6.3-openjdk-11 AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

#set working directory
WORKDIR /build/

RUN mvn package

#copy and run app
FROM openjdk:11-slim as RUN

ARG PORT=5050

WORKDIR /backend/
COPY --from=MAVEN_BUILD /build/target/codesatori_backend-0.0.1-SNAPSHOT.jar /backend/

ENTRYPOINT java -jar –Dserver.port=$PORT codesatori_backend-0.0.1-SNAPSHOT.jar