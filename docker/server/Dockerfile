FROM ubuntu

ARG COOKBOOK_JAR=cookbook-server-0.0.1-SNAPSHOT.jar

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
RUN echo deb http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main | tee /etc/apt/sources.list.d/webupd8team-java.list
RUN echo oracle-java9-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections

RUN apt-get update
RUN apt-get install -y oracle-java9-installer

ADD $COOKBOOK_JAR /app/cookbook-server.jar

EXPOSE 8000

CMD java -jar /app/cookbook-server.jar
