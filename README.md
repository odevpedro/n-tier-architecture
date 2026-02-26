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


# Architecture Patterns #3 — Layered (N-Tier)

## 1. O problema

Quando um sistema cresce sem organização estrutural clara, o caos surge de maneira previsível:

- Regra de negócio dentro de controllers  
- Query SQL misturada com lógica de apresentação  
- Classes gigantes responsáveis por múltiplas funções  
- Dependências cruzadas imprevisíveis  

Qualquer alteração se torna arriscada, porque não existe clareza sobre impacto.

O problema fundamental é a ausência de separação de responsabilidades.

Quando tudo pode chamar tudo, o sistema se transforma em uma **Big Ball of Mud**.

### Big Ball of Mud

```mermaid
flowchart LR
    U[Usuário / Cliente] --> C[Controller]
    C --> S[Service?]
    C --> R[Repository]
    C --> SQL[(SQL / Stored Proc)]
    S --> SQL
    S --> R
    R --> UI[Formatação / ViewModel]
    UI --> C

    subgraph BAG["Big Ball of Mud (tudo chama tudo)"]
      C
      S
      R
      SQL
      UI
    end

    style BAG fill:#ffe6e6,stroke:#cc0000,stroke-width:2px
```

---

## 2. A solução conceitual

A arquitetura em camadas divide o sistema horizontalmente.  
Cada camada possui responsabilidade clara e comunicação direcionada.

Regra fundamental:

- Uma camada conhece apenas a camada imediatamente abaixo.
- A camada de negócio não conhece infraestrutura.
- A apresentação não contém regra de negócio.

### Modelo clássico — 4 camadas

```mermaid
flowchart TB
    P[Presentation Layer\nControllers / REST] --> B[Business Layer\nServices / Use Cases / Rules]
    B --> Pe[Persistence Layer\nRepositories / DAO]
    Pe --> D[(Database Layer\nPostgreSQL / MySQL / MongoDB)]

    classDef layer fill:#eef6ff,stroke:#2b6cb0,stroke-width:1px
    class P,B,Pe,D layer
```

---

### Variação com camada de integração

```mermaid
flowchart TB
    P[Presentation\nREST / Controllers] --> B[Business\nUse Cases / Rules]
    B --> Pe[Persistence\nRepositories]
    Pe --> D[(Database)]

    B --> I[Integration Layer\nHTTP APIs / Kafka / S3]
    I --> EXT[(External Systems)]

    classDef core fill:#e8f5e9,stroke:#2e7d32
    classDef ext fill:#fff3e0,stroke:#ef6c00
    class P,B,Pe,D core
    class I,EXT ext
```

---

## Regra de ouro

A camada de negócio:

- Não sabe quem a chama
- Não sabe qual banco está sendo usado
- Não sabe qual tecnologia externa está envolvida

```mermaid
flowchart LR
    subgraph CALLERS["Callers (não importa quem chama)"]
      REST[REST Controller]
      KAFKA[Kafka Consumer]
      BATCH[Batch Job]
    end

    subgraph BUSINESS["Business Layer (não sabe de infra)"]
      UC[Use Case / Service]
    end

    subgraph SOURCES["Data Sources (não importa qual banco)"]
      PG[(PostgreSQL)]
      MONGO[(MongoDB)]
    end

    REST --> UC
    KAFKA --> UC
    BATCH --> UC

    UC --> REPO[Repository Interface]
    REPO --> PG
    REPO --> MONGO
```

---

## Fluxo correto de execução

```mermaid
sequenceDiagram
    autonumber
    participant Client
    participant Controller as Presentation (Controller)
    participant Service as Business (Service/UseCase)
    participant Repo as Persistence (Repository)
    participant DB as Database

    Client->>Controller: HTTP Request
    Controller->>Service: handle(command/query)
    Service->>Repo: find/save(...)
    Repo->>DB: SQL/Query
    DB-->>Repo: rows/result
    Repo-->>Service: entity/data
    Service-->>Controller: dto/result
    Controller-->>Client: HTTP Response
```

---

## 3. Trade-offs

### Use quando:

- O sistema possui complexidade de negócio moderada
- O time precisa de convenção clara
- Testabilidade é importante
- Você quer estrutura sem over-engineering

### Evite quando:

- O domínio é altamente complexo (DDD e Hexagonal são mais adequados)
- A regra de negócio é trivial
- Performance crítica exige reduzir overhead de camadas

---

## Erros comuns

### 1. Service como repositório glorificado

Quando o Service apenas delega para o Repository sem regra alguma.

```mermaid
flowchart LR
    C[Controller] --> S[Service]
    S --> R[Repository]
    R --> DB[(Database)]

    note1["Service sem regra\nIndício de lógica vazando"]
    S -.-> note1
```

---

### 2. Pular camadas

Quando o Controller chama o Repository diretamente.

```mermaid
flowchart LR
    C[Controller] -->|correto| S[Service]
    S --> R[Repository]
    R --> DB[(Database)]

    C -->|errado| R
    C -->|errado| DB
```

---

## 4. Case — eBay

O eBay, no início dos anos 2000, operava como um grande monolito com regras espalhadas.

Antes de migrar para distribuição e serviços, a primeira grande refatoração foi impor disciplina arquitetural em camadas dentro do próprio monolito.

Essa separação permitiu:

- Identificar fronteiras claras
- Isolar responsabilidades
- Extrair serviços com segurança

```mermaid
flowchart LR
    A[Monolito caótico] --> B[Monolito em camadas]
    B --> C[Extração gradual de serviços]
    C --> D[Arquitetura distribuída com boundaries claros]
```

---

## Conclusão

Você não salta de Big Ball of Mud diretamente para microsserviços.

Layered Architecture é frequentemente o passo intermediário necessário para:

- Estabilizar fronteiras
- Organizar responsabilidades
- Tornar o sistema testável
- Permitir evolução arquitetural futura

Ela não é a solução final para todo sistema, mas é a base estrutural mais comum e didática da engenharia de software moderna.
