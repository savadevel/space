FROM maven:3.6.3-jdk-11-slim AS builder

ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

COPY ./pom.xml $PROJECT_DIR
RUN mvn dependency:resolve

COPY ./src $PROJECT_DIR/src
RUN mvn -e -X install

FROM tomcat:9.0.43-jdk11-openjdk-slim

ENV PROJECT_DIR=/opt/project
ENV WEBAPPS_DIR=/usr/local/tomcat/webapps/

COPY --from=builder $PROJECT_DIR/target/cosmoport.war $WEBAPPS_DIR/

# https://github.com/jwilder/dockerize
RUN apt-get update && apt-get install -y wget
ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

EXPOSE 8080

CMD ["dockerize", "-wait", "tcp://db:3306", "-timeout", "20s", "catalina.sh", "run"]