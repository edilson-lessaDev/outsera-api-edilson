# Outsera API - Intervalo de Produtores

API REST desenvolvida em **Java 8 + Spring Boot 2.7** para cálculo do intervalo entre vitórias de produtores de filmes.

A aplicação está configurada para rodar na porta **8081** (application.properties).

---

## Visão Geral

A aplicação processa dados de filmes vencedores e identifica:

- Produtores com o **menor intervalo** entre vitórias consecutivas  
- Produtores com o **maior intervalo** entre vitórias consecutivas  

---

##  Stack Tecnológica

- Java 8  
- Spring Boot 2.7.18  
- Spring Data JPA  
- H2 Database (em memória)  
- Maven  
- JUnit 5 + MockMvc  

---

## Execução do Projeto

### Pré-requisitos

- Java 8 instalado  
- Maven configurado  

### Build

```bash
mvn clean package
```

### Execução

```bash
java -jar target/outsera-api-1.0.0-SNAPSHOT.jar
```

---

## API

### Endpoint

GET /producers/intervals

### URL

http://localhost:8081/producers/intervals

---

## Exemplo de Resposta

```json
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    }
  ],
  "max": [
    {
      "producer": "Matthew Vaughn",
      "interval": 13,
      "previousWin": 2002,
      "followingWin": 2015
    }
  ]
}
```

---

## Persistência

A aplicação utiliza banco em memória **H2**, com carga inicial baseada em arquivo CSV.

Console:
http://localhost:8081/h2-console

---

## Testes

Testes de integração com SpringBootTest + MockMvc:

- 200 OK  
- 404 Not Found  
- 405 Method Not Allowed  

```bash
mvn test
```

---

## Arquitetura

- Controller → endpoints REST  
- Service → regras de negócio  
- Repository → acesso a dados  
- DTO → resposta da API  

---

## Padrão REST

Implementação baseada no nível 2 de maturidade de Richardson Conforme requisitado
na documentação. 

---

## Autor
Projeto desenvolvido para avaliação técnica.                            
Edilson de Oliveira Lessa