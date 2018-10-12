FROM ubuntu

ARG JDK_PATH=docker/cookbook-server/jdk1.8.0_181/
ARG COOKBOOK_JAR=build/libs/cookbook-server-0.0.1-SNAPSHOT.jar

COPY $JDK_PATH /opt/jdk/
COPY $COOKBOOK_JAR /app/cookbook-server.jar

EXPOSE 8080

CMD /opt/jdk/bin/java -jar -Dspring.profiles.active=dell-dm051 /app/cookbook-server.jar
