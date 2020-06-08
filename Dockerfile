#pull official docker image for ReactJS
FROM maven:3.6.3-openjdk-11 AS MAVEN_BUILD

#set working directory
WORKDIR /build/

COPY . ./

RUN export GOOGLE_APPLICATION_CREDENTIALS="/cred.json"
RUN mvn package

#copy and run app
FROM openjdk:11-slim as RUN

WORKDIR /backend/
COPY --from=MAVEN_BUILD /build/target/codesatori_backend-0.0.1-SNAPSHOT.jar /backend/
COPY --from=MAVEN_BUILD /cred.json /
RUN export GOOGLE_APPLICATION_CREDENTIALS="/cred.json"

CMD java -jar -Dserver.port=$PORT codesatori_backend-0.0.1-SNAPSHOT.jar