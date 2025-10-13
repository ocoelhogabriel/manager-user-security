# Melhores Práticas para Clean Architecture

Este documento fornece diretrizes e melhores práticas para trabalhar com Clean Architecture no projeto Manager User Security.

## Princípios Fundamentais

### Regra de Dependência

O princípio mais importante da Clean Architecture é a **Regra de Dependência**:

> As dependências de código-fonte só podem apontar para dentro, em direção às políticas de alto nível.

Isso significa:

1. O código nas camadas externas pode depender das camadas internas
2. O código nas camadas internas **nunca** deve depender das camadas externas
3. Entidades e casos de uso não sabem nada sobre bancos de dados, frameworks ou interfaces

### Independência de Frameworks

O sistema não deve ser acoplado a frameworks específicos:

- Frameworks são ferramentas, não a arquitetura
- O sistema deve funcionar sem frameworks específicos
- Frameworks devem ser "plugáveis" na arquitetura

### Testabilidade

A arquitetura deve facilitar os testes:

- Regras de negócio devem ser testáveis sem UI, banco de dados ou outros elementos externos
- Testes devem ser rápidos e não depender de infraestrutura externa

### Independência de UI

A interface do usuário deve ser fácil de mudar:

- A substituição da interface web por uma API deve ser possível sem alterar as regras de negócio
- As mudanças na UI não devem forçar mudanças nas camadas internas

### Independência de Banco de Dados

O sistema não deve ser acoplado a um banco de dados específico:

- A troca de PostgreSQL para outro banco de dados deve afetar apenas a camada de infraestrutura
- As regras de negócio não devem saber qual banco de dados está sendo usado

## Camadas da Arquitetura

### 1. Entidades (Domain)

#### Melhores Práticas:

- Mantenha as entidades ricas em comportamento, não apenas recipientes de dados
- Encapsule regras de negócio invariáveis nas entidades
- Use Value Objects para conceitos de domínio que não têm identidade
- Evite anotações de frameworks nas entidades de domínio
- Não inclua detalhes de persistência (JPA, etc.) em entidades de domínio

#### Padrões Recomendados:

- Value Objects
- Domain Services
- Factories
- Repositories (interfaces)

### 2. Casos de Uso (Application)

#### Melhores Práticas:

- Um caso de uso deve realizar uma única ação do usuário
- Use DTOs para entrada e saída dos casos de uso
- Casos de uso devem orquestrar entidades para realizar um objetivo
- Evite lógica de negócio nos casos de uso (isso pertence às entidades)
- Use interfaces para dependências externas (repositórios, serviços)

#### Padrões Recomendados:

- Command Pattern
- Mediator Pattern
- Service Layer
- DTO Pattern

### 3. Adaptadores de Interface (Interface Adapters)

#### Melhores Práticas:

- Separe claramente adaptadores de entrada (controllers) e saída (gateways)
- Converta dados de formatos externos para formatos internos e vice-versa
- Mantenha adaptadores leves - eles não devem conter lógica de negócio
- Implemente adaptadores para interfaces definidas nas camadas internas

#### Padrões Recomendados:

- Adapter Pattern
- Facade Pattern
- MVC/MVP Pattern

### 4. Frameworks e Drivers (Infrastructure)

#### Melhores Práticas:

- Isole frameworks em seus próprios pacotes
- Use padrão Adapter para integrar frameworks com a aplicação
- Mantenha código específico de framework na camada mais externa possível
- Implemente interfaces definidas pelas camadas internas

#### Padrões Recomendados:

- Repository Implementation
- Adapter Pattern
- Factory Pattern

## Implementação Prática

### Como Garantir a Direção de Dependência Correta

1. **Use Interfaces para Inversão de Dependência**:
   ```java
   // Definida na camada de domínio
   public interface UsuarioRepository {
       Optional<Usuario> findByEmail(String email);
   }
   
   // Implementada na camada de infraestrutura
   @Repository
   public class UsuarioRepositoryImpl implements UsuarioRepository {
       private final UsuarioJpaRepository jpaRepository;
       
       @Override
       public Optional<Usuario> findByEmail(String email) {
           return jpaRepository.findByEmail(email).map(this::mapToDomain);
       }
   }
   ```

2. **Use Mappers para Conversão de Objetos Entre Camadas**:
   ```java
   // Na camada de infraestrutura
   private Usuario mapToDomain(UsuarioEntity entity) {
       return Usuario.builder()
           .id(entity.getId())
           .nome(entity.getNome())
           .email(entity.getEmail())
           .build();
   }
   ```

3. **Use DTOs para Transferência de Dados**:
   ```java
   // Na camada de application
   public class CriarUsuarioDTO {
       private String nome;
       private String email;
       private String senha;
       // getters, setters
   }
   ```

4. **Use Casos de Uso para Operações de Negócio**:
   ```java
   public class CriarUsuarioUseCase {
       private final UsuarioRepository repository;
       private final CriptografiaService criptografia;
       
       public UsuarioDTO execute(CriarUsuarioDTO dto) {
           // Validar dados
           // Criar entidade de domínio
           // Salvar via repositório
           // Retornar DTO de resposta
       }
   }
   ```

### Como Evitar Dependências Circulares

1. **Detecção**:
   - Use ferramentas como JDepend para detectar ciclos
   - Configure validação em tempo de build
   - Documente dependências circulares encontradas

2. **Resolução**:
   - Refatore para remover a dependência circular
   - Use eventos de domínio para comunicação assíncrona
   - Considere reorganizar responsabilidades
   - Introduza uma abstração intermediária

### Organizando Pacotes e Módulos

Existem duas abordagens principais para organizar pacotes:

#### 1. Organização por Camada

```
com.example.app/
├── domain/
│   ├── usuario/
│   └── perfil/
├── application/
│   ├── usuario/
│   └── perfil/
├── interfaces/
│   ├── api/
│   └── messaging/
└── infrastructure/
    ├── persistence/
    └── security/
```

#### 2. Organização por Feature

```
com.example.app/
├── usuario/
│   ├── domain/
│   ├── application/
│   ├── interfaces/
│   └── infrastructure/
└── perfil/
    ├── domain/
    ├── application/
    ├── interfaces/
    └── infrastructure/
```

**Recomendação**: Use organização por camada para projetos menores e médios. Para projetos grandes, considere uma abordagem híbrida ou baseada em features.

## Dicas para Revisões de Código

Ao revisar código em um projeto de Clean Architecture, verifique:

1. **Direção de Dependência**: O código segue a regra de dependência? As camadas internas não dependem das externas?

2. **Responsabilidade por Camada**: Cada componente está na camada correta de acordo com sua responsabilidade?

3. **Objetos de Domínio**: As entidades contêm comportamento, não apenas dados? As regras de negócio estão nas entidades?

4. **Casos de Uso**: Os casos de uso orquestram entidades sem conter regras de negócio próprias?

5. **Adaptadores**: Os adaptadores convertem entre formatos externos e internos sem conter lógica de negócio?

6. **Frameworks**: Os frameworks estão isolados na camada externa?

## Armadilhas Comuns a Evitar

1. **Entidades Anêmicas**: Entidades sem comportamento, apenas com getters e setters

2. **Lógica de Negócio em Controllers**: Regras de negócio devem estar nas entidades ou serviços de domínio

3. **Vazamento de Conceitos de Infraestrutura**: JPA em entidades de domínio, SQL em serviços de aplicação

4. **DTOs Excessivos**: Criação de DTOs desnecessários que apenas passam dados

5. **Casos de Uso Complexos**: Casos de uso que fazem mais de uma coisa

6. **Dependência Direta de Frameworks**: Uso direto de frameworks nas camadas internas

## Conclusão

A Clean Architecture é um conjunto de princípios, não um conjunto rígido de regras. O mais importante é manter a direção de dependência correta e garantir que as regras de negócio sejam independentes de detalhes técnicos.

Use estas diretrizes como um guia, mas adapte conforme necessário para o seu contexto específico. A arquitetura deve servir às necessidades do projeto, não o contrário.
