FROM ubuntu

RUN apt-get update
RUN apt-get install -y mailutils rsyslog

RUN echo cookbook.com > /etc/mailname
RUN sed -i 's/mynetworks =/mynetworks = 192.168.0.0\/24 172.17.0.0\/16/g' /etc/postfix/main.cf
RUN sed -i 's/inet_protocols = all/inet_protocols = ipv4/g' /etc/postfix/main.cf

EXPOSE 25

CMD service rsyslog start && service postfix start && sleep 1 && tail -f /var/log/mail.log
