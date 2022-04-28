FROM openjdk:11-jre-slim

ENV ES_HOST "elasticsearch"

ENV BEARER_TOKEN "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIzNWM4M2FlMGM0MmE0MGI2YmRkZDQzYzcxMTg2NjU1YiIsImlhdCI6MTY1MDk1MjY2NSwiZXhwIjoxOTY2MzEyNjY1fQ.R7asxItssyei_i-3VHjNEvqtaOVfSUAX1dx7XMjMA-4"

ENV HASS_HOST "http://192.168.28.83:8123"

ENV SERVER_PORT 8080
EXPOSE ${SERVER_PORT}


ARG JAR_FILE=target/udo-api-rest-0.0.1-spring-boot.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","--elasticsearch.host=${ES_HOST}","--httpGateway.bearer.token=${BEARER_TOKEN}","--homeassistant.host=${HASS_HOST}"]