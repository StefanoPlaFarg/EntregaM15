# Entrega M15
Entrega M15 sobre Spring Boot, Spring Data i Spring Security amb JWT.

### Teoria
S'ha aplicat la següent teoria apresa durant el curs:
* Patrons de disseny: Singleton (Spring), Builder, MVC, Disseny per capes,  DTO, DAO, etc.
* Principis i bones pràctiques:  CRUD, ús d'excepcions i DDD i SOLID (en la major mesura possible)
* Patrons d'arquitectura: Microserveis
* Llenguatge: s'ha usat Java 8 (lambdas i streams, optionals)

### Millores en un futur
* Aplicar TDD i usant JUNIT5 i Mockito2
* Refactoring del codi
* Creació d'excepcions per gestionar les no autoritzacions a certes APIS
* Automatizació de tests amb Postman

## Descripció
* S'ha desenvolupat fins al nivell 1.

### Nivell 1
* Fase 1 -> Branca fase1 (MySQL + Spring Security + JWT)
* Fase 2 -> Branca fase2 (MongoDB + Spring Security + JWT )
* Fase 3 -> Inclós en la fase 1 i fase 2

## Prerequisits
* Per desenvolupar el projecte s'ha basat en Spring Initializr (https://start.spring.io/) ja que el projecte descarregat de l'enllaÇ https://github.com/IT-Academy-BCN/springBootInitialDemo donava problemes al compilar.
* Utilitzar la col.lecció de Postman per testejar l'API: EntregaM15.postman_collection.json
* Per la fase 1 , serà necessari instal·lar MySQL i crear una instancia amb un password i usuari determinat i crear una BBDD amb el nom
entregam15. L'usuari i el password s'hauran d'intorudir a l'arxiu application.properties del projecte en els següents camps:
- spring.datasource.username= put your username
- spring.datasource.password= put your password
* Per la fase 2 , s'haurà d'instal·lar MongoDB 4.2.12  crear una BBDD amb el nom entregam15
* Instal·lar el JAR Lombok a Eclipse per la creació automatica de Constructors, Getters, Setters, Builders etc.
https://www.adictosaltrabajo.com/2016/02/03/como-reducir-el-codigo-repetitivo-con-lombok/

## Testing 
* Importar les següents  col·leccions a Postman per testejar l'API: 
  -EntregaM15_MySQL+JWT.postman_collection.json
  -EntregaM15_MongoDB+JWT.postman_collection.json
  
 * Es pot utilitzar també Swagger:
 Anar a la següent URL al navegador: http://localhost:8080/api/v1/swagger-ui.html# un cop s'hagi executat el programa. Des d'allà es podrà utilitzar l'API tal com a Postman.


