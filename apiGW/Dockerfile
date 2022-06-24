FROM adoptopenjdk/openjdk11
ADD target/apiGW.jar api-gateway.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]