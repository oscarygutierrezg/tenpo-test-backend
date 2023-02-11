# tempo-test-backend

## Despliegue en Local
- Debe tener instalado docker y corriendo luego ejecutar
```
$ docker run --name some-postgres -p 5432:5432 -e POSTGRES_USER=root -e POSTGRES_DB=postgres -e POSTGRES_PASSWORD=root -d postgres
```
```
$ docker run -d --name some-redis -p 6379:6379 redis/redis-stack-server:latest
```
- Buscar el repositorio tempo-test-backend-mock disponible [aqui](https://github.com/oscarygutierrezg/tempo-test-backend-mock) luego de descargarlo debe seguir las instrucciones de despliegue en local
- Finalmente ejecutar:

```
    $ mvn clean spring-boot:run
```
## Documentacion del API
La documentación en formato OpenAPI está disponible [aqui](http://localhost:8080/tempo/swagger-ui/index.html) </br>
La información de actuator está disponible [aqui](http://localhost:8080/tempo/actuator) </br>
La información sobre los reintentos está disponible [aqui](http://localhost:8080/tempo/actuator/retryevents) </br>