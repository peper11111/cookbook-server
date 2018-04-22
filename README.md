# cookbook-server

> A Spring Boot project

## Build Setup
```bash
# build project
./gradlew assemble

# build docker images
docker build -f docker/postgres/Dockerfile -t cookbook-postgres .
docker build -f docker/postfix/Dockerfile -t cookbook-postfix .
docker build -f docker/server/Dockerfile -t cookbook-server .

# create docker containers
docker create --name cookbook-postgres -p 5432:5432 -it cookbook-postgres
docker create --name cookbook-postfix -p 25:25 -it cookbook-postfix
docker create --name cookbook-server -p 8000:8000 -it cookbook-server

# start docker containers
docker start cookbook-postgres
docker start cookbook-postfix
docker start cookbook-server
```
