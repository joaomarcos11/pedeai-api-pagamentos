# Pagamentos Pedeai API

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: [https://quarkus.io/](https://quarkus.io/).

## Running the Application with Docker Compose

You can run the application using Docker Compose. This will set up the necessary services and run the application in a containerized environment.

### Prerequisites

- Docker
- Docker Compose

### Steps

1. Clone the repository:
    ```sh
    git clone git@github.com:joaomarcos11/pedeai-api-pagamentos.git
    cd pedeai-api-pagamentos
    ```

2. Build the Docker images and start the services:
    ```sh
    docker-compose up --build
    ```

3. The application should now be running. You can access it at `http://localhost:8080`.

## Building the Application

You can build the application using Maven. This will compile the code, run the tests, and package the application.

### Steps

1. Build the application:
    ```sh
    mvn clean package
    ```

2. The packaged application will be available in the `target` directory.

## Hexagonal Architecture

This project follows the principles of Hexagonal Architecture (also known as Ports and Adapters Architecture). The main goal of this architecture is to create loosely coupled application components that can be easily tested and maintained.

### Key Concepts

- **Domain Layer**: Contains the core business logic and domain entities. This layer is independent of any external systems or frameworks.
- **Application Layer**: Contains the application services that orchestrate the business logic. This layer interacts with the domain layer and external systems through ports.
- **Ports**: Interfaces that define the input and output boundaries of the application. Ports are implemented by adapters.
- **Adapters**: Implementations of the ports that interact with external systems (e.g., databases, messaging systems, external APIs).

### Project Structure

- `src/main/java/com/org/jfm/domain`: Contains the domain entities and business logic.
- `src/main/java/com/org/jfm/application`: Contains the application services.
- `src/main/java/com/org/jfm/ports`: Contains the port interfaces.
- `src/main/java/com/org/jfm/adapters`: Contains the adapter implementations.
- `src/main/java/com/org/jfm/controllers`: Contains the REST controllers.

### Example

- **Domain Layer**: `Pagamento` entity and business logic.
- **Application Layer**: `PagamentoService` orchestrates the business logic.
- **Ports**: `PagamentoRepository` and `MercadoPagoApi` interfaces.
- **Adapters**: `MongoPagamentoRepository` and `MercadoPagoApiAdapter` implement the ports.
- **Controllers**: `PagamentoResource` exposes the REST endpoints.

## Running the Application in Dev Mode

You can run your application in dev mode that enables live coding using:
```sh
./mvnw quarkus:dev
```

## Testing

You can run the tests using Maven:
```sh
mvn test
```
