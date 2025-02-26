# Batchie

Batchie is a web application designed to handle and process shipment events through webhooks. It supports both SOAP and RESTful webhooks, allowing for flexible integration with various shipment tracking systems.

## Features

- **Webhook Handling**: Supports SOAP, RESTful, and TrackingMore webhooks.
- **Shipment Management**: Provides endpoints to create and retrieve shipment details.
- **Logging**: Logs important events and errors for monitoring and debugging.
- **API Documentation**: Automatically generated API documentation using Swagger.

## Endpoints

### Webhook Endpoint

- **POST /webhook**: Handles incoming webhook requests. Supports SOAP, REST, and TrackingMore content types.

### Shipment Endpoints

- **GET /shipments/{id}**: Retrieves shipment details by ID.
- **POST /shipments**: Creates a new shipment.

## Getting Started

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/batchie.git
    ```

2. Navigate to the project directory:
    ```bash
    cd batchie
    ```

3. Build the project:
    ```bash
    ./mvnw clean install
    ```

4. Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```

5. Access the Swagger API documentation:
    Open your browser and navigate to `http://localhost:8080/swagger-ui.html`

## Dependencies

- Spring Boot
- Lombok
- Jackson
- SLF4J
- Swagger

## License

This project is licensed under the MIT License.
