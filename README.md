# layered-architecture-pattern

Reference implementation of **Layered (N-Tier) Architecture**.

## Run (Docker)
```bash
docker compose up --build
```

## Endpoints
- POST /tasks
  ```json
  { "title": "estudar layered architecture" }
  ```
- GET /tasks

## Package structure
- presentation: controllers + DTOs
- business: rules/use cases/services
- persistence: entities + repositories
- integration: external clients (HTTP/Kafka/S3)
