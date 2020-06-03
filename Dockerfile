#pull official docker image for ReactJS
FROM maven:3.6.3-openjdk-11 AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

#set working directory
WORKDIR /build/

RUN mvn package

#copy and run app
FROM openjdk:11-alpine as RUN

ARG JAR_FILE=codesatori_backend-0.0.1-SNAPSHOT.jar

WORKDIR /backend/
COPY --from=MAVEN_BUILD /build/target/${JAR_FILE} /backend/

ENTRYPOINT ["java","-jar",${JAR_FILE}]
CMD ["–server.port=$PORT"]