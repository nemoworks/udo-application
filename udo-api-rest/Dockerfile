FROM openjdk:11-jre-slim

ENV ES_HOST elasticsearch
#
ENV SERVER_PORT 8080
EXPOSE ${SERVER_PORT}


#
#ENV RESOURCE_UID 1234567
##ENV SPRING_BOOT_ADMIN_HOST http://localhost:8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","--elasticsearch.host=${ES_HOST}"]