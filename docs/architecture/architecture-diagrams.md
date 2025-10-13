# Diagrama de Arquitetura - Manager User Security

Este documento apresenta os diagramas da arquitetura do sistema Manager User Security.

## Diagrama de Camadas (Clean Architecture)

```ascii
+----------------------------------------------------------+
|                                                          |
|  +--------------------------------------------------+    |
|  |                                                  |    |
|  |  +------------------------------------------+    |    |
|  |  |                                          |    |    |
|  |  |  +----------------------------------+    |    |    |
|  |  |  |                                  |    |    |    |
|  |  |  |           DOMAIN                 |    |    |    |
|  |  |  |  +-------------------------+     |    |    |    |
|  |  |  |  |                         |     |    |    |    |
|  |  |  |  |       Entidades         |     |    |    |    |
|  |  |  |  |      Value Objects      |     |    |    |    |
|  |  |  |  |  Interfaces Repositório |     |    |    |    |
|  |  |  |  |                         |     |    |    |    |
|  |  |  |  +-------------------------+     |    |    |    |
|  |  |  |                                  |    |    |    |
|  |  |  +----------------------------------+    |    |    |
|  |  |                APPLICATION               |    |    |
|  |  |      Use Cases, DTOs, Serviços App      |    |    |
|  |  |                                          |    |    |
|  |  +------------------------------------------+    |    |
|  |                 INTERFACE ADAPTERS                |    |
|  |       Controllers, Presenters, Gateways           |    |
|  |                                                  |    |
|  +--------------------------------------------------+    |
|                FRAMEWORKS & DRIVERS                       |
|   Spring Boot, Spring Security, JPA/Hibernate, PostgreSQL |
|                                                          |
+----------------------------------------------------------+
```

## Diagrama de Fluxo de Autenticação JWT

```ascii
+--------+                               +----------------+
|        |--(1)- Credenciais Login ----->|                |
| Client |                               | Authentication |
|        |<-(2)-- Token JWT -------------|    Controller  |
+--------+                               +----------------+
    |                                            |
    |                                            v
    |                                    +----------------+
    |                                    |                |
    |                                    | Authentication |
    |                                    |    Service     |
    |                                    +----------------+
    |                                            |
    v                                            v
+--------+                               +----------------+
|        |--(3)- Request com Token ----->|                |
| Client |                               | JWTAuthFilter  |
|        |<-(4)-- Response --------------|                |
+--------+                               +----------------+
                                                  |
                                                  v
                                         +----------------+
                                         |                |
                                         |  JWT Service   |
                                         |                |
                                         +----------------+
                                                  |
                                                  v
                                         +----------------+
                                         |                |
                                         | SecurityContext|
                                         |                |
                                         +----------------+
```

## Diagrama de Dependências Entre Componentes

```ascii
                                 +-------------+
                                 |             |
                            +--->+ Controllers |
                            |    |             |
                            |    +-------------+
                            |            |
                            |            v
+--------------+    +---------------+    +-------------+
|              |    |               |    |             |
| Domain Model |<---+ Application   |<---+ Repositories|
|              |    | Services/     |    | (Interface) |
+--------------+    | Use Cases     |    +-------------+
                    |               |            ^
                    +---------------+            |
                                                 |
                                         +----------------+
                                         |                |
                                         | JPA Repository |
                                         | Implementation |
                                         |                |
                                         +----------------+
```

## Diagrama de Implantação

```ascii
                      +---------------------+
                      |                     |
                      |   Load Balancer     |
                      |                     |
                      +----------+----------+
                                 |
           +-------------------+ | +-------------------+
           |                   | | |                   |
           |  App Server 1     | | |  App Server 2     |
           |  Spring Boot      +---+  Spring Boot      |
           |                   | | |                   |
           +--------+----------+ | +----------+--------+
                    |            |            |
                    |            |            |
                    v            v            v
           +----------------------------------------+
           |                                        |
           |            PostgreSQL Cluster          |
           |            (Master/Replica)            |
           |                                        |
           +----------------------------------------+
```

## Diagrama de Fluxo de Solicitação

```ascii
  +--------+                            +----------------+
  |        |                            |                |
  | Client +--------------------------->+ Controller     |
  |        |                            |                |
  +--------+                            +------+---------+
                                               |
                                               v
                                      +----------------+
                                      |                |
                                      | Use Case       |
                                      |                |
                                      +------+---------+
                                              |
                    +-------------------------+-------------------------+
                    |                                                   |
                    v                                                   v
          +------------------+                                +------------------+
          |                  |                                |                  |
          | Domain Service   |                                | Repository       |
          |                  |                                | Interface        |
          +--------+---------+                                +--------+---------+
                   |                                                   |
                   |                                                   v
                   |                                         +-------------------+
                   |                                         |                   |
                   |                                         | Repository Impl   |
                   |                                         |                   |
                   |                                         +--------+----------+
                   |                                                  |
                   |                                                  v
                   |                                          +----------------+
                   |                                          |                |
                   +----------------------------------------->+ Domain Entity  |
                                                             |                |
                                                             +----------------+
```

## Diagrama de Componentes do Sistema

```ascii
+---------------------------------------------------+
|                                                   |
|                 API REST Layer                    |
|                                                   |
+---+-------------------------------+---------------+
    |                               |
    v                               v
+---+---------------+   +-----------+--------------+
|                   |   |                          |
| Usuario Controller|   | Autenticação Controller  |
|                   |   |                          |
+-------------------+   +-----------+--------------+
    |                               |
    v                               v
+---+---------------+   +-----------+--------------+
|                   |   |                          |
|  Usuario Use Case |   |  Auth Service            |
|                   |   |                          |
+---+---------------+   +-----------+--------------+
    |                               |
    v                               v
+---+---------------+   +-----------+--------------+
|                   |   |                          |
| Usuario Repository|   |  JWT Token Service       |
|                   |   |                          |
+-------------------+   +--------------------------+
    |
    v
+---+---------------+
|                   |
|  Database         |
|                   |
+-------------------+
```

## Nota Sobre os Diagramas

Estes diagramas são representações simplificadas da arquitetura e dos fluxos no sistema. Para uma compreensão mais detalhada, consulte a documentação de arquitetura e os ADRs.
