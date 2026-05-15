# pet-clinic

Backend REST do projeto PetClinic, construido com Spring Boot 4, JPA/Hibernate, Flyway e SQLite.

## Stack

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA + Hibernate
- Flyway (migrations SQL)
- SQLite (runtime)
- H2 (apenas testes)
- OpenAPI/Swagger UI

## Banco de dados

- Runtime: SQLite em arquivo local `data/petclinic.db`
- Migrations Flyway:
  - `src/main/resources/db/migration/V1__create_schema.sql`
  - `src/main/resources/db/migration/V2__seed_initial_data.sql`
- Testes: H2 em memoria

## Configuracao por ambiente

Arquivo principal: `src/main/resources/application.properties`

Variaveis relevantes:

- `CORS_ALLOWED_ORIGINS`: lista separada por virgula com as origens permitidas no CORS

Exemplo em `.env.example`:

- `CORS_ALLOWED_ORIGINS=origins`
- `LOCAL_UID=`
- `LOCAL_GID=`

`LOCAL_UID` e `LOCAL_GID` sao opcionais para controlar permissao de escrita no bind mount do SQLite em Docker.

## Executar local (sem Docker)

Requisitos:

- Java 21
- Maven Wrapper (ja incluso no projeto)

Comandos:

```bash
./mvnw clean test
./mvnw spring-boot:run
```

Aplicacao: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Executar com Docker

Requisitos:

- Docker + Docker Compose

Na pasta `pet-clinic`:

```bash
docker compose up --build -d
```

Portas:

- Host `7272` -> Container `8080`

Volume:

- `./data:/app/data` (persistencia do SQLite)

Parar:

```bash
docker compose down
```

## API

Prefixo principal dos endpoints: `/api`

Exemplos:

- `GET /api/owners`
- `POST /api/owners`
- `POST /api/owners/{ownerId}/pets/{petId}/visits`
- `GET /api/vets`

## Regras de validacao implementadas

- Telefone de proprietario: somente 10 ou 11 digitos
- Data de visita: deve ser futura
