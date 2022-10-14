## Maven

### Create .jar file
````
mvn clean package
````

## Create Docker Network
```
docker network create -d bridge --subnet=192.168.0.0/24 --gateway=192.168.0.1 rede_stf
```

## Create Docker Image
````
docker build -t convergencia/cpf .
````

## Run Docker Container
```
docker run --network=rede_stf --ip 192.168.0.45 -it -p 8080:8080 convergencia/cpf
```