FROM openjdk:8

ARG COOKBOOK_JAR=build/libs/cookbook-server-0.0.1-SNAPSHOT.jar
COPY $COOKBOOK_JAR /app/cookbook-server.jar

EXPOSE 8080

CMD java -jar -Dspring.profiles.active=docker /app/cookbook-server.jar
