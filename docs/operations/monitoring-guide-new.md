# Guia de Monitoramento e Observabilidade

Este guia descreve as práticas recomendadas e configurações para monitoramento e observabilidade do sistema Manager User Security.

## Introdução

Um sistema de monitoramento e observabilidade eficaz é essencial para garantir a confiabilidade, disponibilidade e performance do Manager User Security em ambientes de produção. Este guia abrange:

1. Coleta de métricas
2. Geração e coleta de logs
3. Rastreamento distribuído
4. Alertas
5. Dashboards e visualizações

## Pilares da Observabilidade

A observabilidade do sistema é baseada em três pilares principais:

- **Métricas**: Dados numéricos que representam o estado do sistema em um momento específico
- **Logs**: Registros de eventos que ocorreram no sistema
- **Traces**: Informações sobre o fluxo de execução de requisições através dos componentes do sistema

## 1. Coleta de Métricas

### 1.1. Métricas Expostas pela Aplicação

O Manager User Security expõe métricas através do Spring Boot Actuator no endpoint `/actuator/prometheus`. Estas incluem:

- Métricas JVM (uso de memória, threads, garbage collection)
- Métricas HTTP (contagem de requisições, latência, códigos de status)
- Métricas de conexões de banco de dados (pool de conexões, tempos de execução de consultas)
- Métricas personalizadas de negócios (autenticações, operações de usuários)

### 1.2. Configuração do Prometheus

Para coletar métricas com Prometheus:

```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'manager-user-security'
    metrics_path: '/manager_user_security/actuator/prometheus'
    static_configs:
      - targets: ['manager-user-security:8080']
```

Para implantação com Docker Compose:

```yaml
# Trecho do docker-compose.yml
prometheus:
  image: prom/prometheus:latest
  container_name: prometheus
  volumes:
    - ./prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
    - prometheus-data:/prometheus
  ports:
    - "9090:9090"
  networks:
    - manager-network
```

### 1.3. Métricas Personalizadas

Para implementar métricas personalizadas de negócio, utilize o MeterRegistry do Micrometer:

```java
@Service
public class UserMetricsService {
    
    private final Counter authenticationSuccessCounter;
    private final Counter authenticationFailureCounter;
    private final Timer userOperationsTimer;
    
    public UserMetricsService(MeterRegistry registry) {
        authenticationSuccessCounter = registry.counter("security.authentication.success");
        authenticationFailureCounter = registry.counter("security.authentication.failure");
        userOperationsTimer = registry.timer("user.operations");
    }
    
    public void recordSuccessfulAuthentication() {
        authenticationSuccessCounter.increment();
    }
    
    public void recordFailedAuthentication() {
        authenticationFailureCounter.increment();
    }
    
    public <T> T recordUserOperationTime(Supplier<T> operation) {
        return userOperationsTimer.record(operation);
    }
}
```

## 2. Geração e Coleta de Logs

### 2.1. Configuração de Logs

O sistema utiliza SLF4J com Logback para geração de logs. A configuração principal está em `src/main/resources/logback-spring.xml`:

```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    </appender>
    
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/application.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/application.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>
    
    <logger name="com.ocoelhogabriel.manager_user_security" level="DEBUG"/>
</configuration>
```

### 2.2. Boas Práticas para Logging

- **Níveis de Log Apropriados**:
  - ERROR: Erros que impedem a operação normal
  - WARN: Situações anormais, mas que não impedem a operação
  - INFO: Eventos significativos do ponto de vista operacional
  - DEBUG: Informações úteis para debugging
  - TRACE: Detalhes granulares para diagnóstico profundo

- **Contextualização**:
  - Incluir identificadores de correlação (correlation IDs)
  - Informar o usuário associado à operação (quando aplicável)
  - Registrar dados relevantes para diagnóstico, sem expor informações sensíveis

- **Logs Estruturados**:
  - Utilizar formato JSON para logs (via LogstashEncoder)
  - Incluir metadados como timestamp, nível, thread, e classe
  - Adicionar campos estruturados em vez de concatenar strings

### 2.3. Coleta de Logs com ELK Stack

Para coletar e analisar logs com ELK Stack (Elasticsearch, Logstash, Kibana):

```yaml
# Trecho do docker-compose.yml para ELK Stack
elasticsearch:
  image: docker.elastic.co/elasticsearch/elasticsearch:7.14.0
  container_name: elasticsearch
  environment:
    - discovery.type=single-node
    - xpack.security.enabled=false
  volumes:
    - elasticsearch-data:/usr/share/elasticsearch/data
  ports:
    - "9200:9200"
  networks:
    - manager-network

logstash:
  image: docker.elastic.co/logstash/logstash:7.14.0
  container_name: logstash
  volumes:
    - ./logstash/pipeline:/usr/share/logstash/pipeline
  ports:
    - "5044:5044"
  networks:
    - manager-network
  depends_on:
    - elasticsearch

kibana:
  image: docker.elastic.co/kibana/kibana:7.14.0
  container_name: kibana
  ports:
    - "5601:5601"
  environment:
    - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
  networks:
    - manager-network
  depends_on:
    - elasticsearch
```

Configuração do pipeline Logstash (`logstash/pipeline/logstash.conf`):

```
input {
  tcp {
    port => 5044
    codec => json
  }
}

filter {
  if [logger_name] =~ "com.ocoelhogabriel.manager_user_security" {
    mutate {
      add_field => { "application" => "manager-user-security" }
    }
  }
}

output {
  elasticsearch {
    hosts => ["elasticsearch:9200"]
    index => "manager-user-security-%{+YYYY.MM.dd}"
  }
}
```

### 2.4. Configuração Alternativa com Loki

Para ambientes Kubernetes ou mais leves, Grafana Loki é uma alternativa ao ELK Stack:

```yaml
# Trecho do docker-compose.yml para Loki
loki:
  image: grafana/loki:2.4.0
  container_name: loki
  ports:
    - "3100:3100"
  command: -config.file=/etc/loki/local-config.yaml
  networks:
    - manager-network

promtail:
  image: grafana/promtail:2.4.0
  container_name: promtail
  volumes:
    - ./logs:/var/log
    - ./promtail/config.yml:/etc/promtail/config.yml
  command: -config.file=/etc/promtail/config.yml
  networks:
    - manager-network
```

## 3. Rastreamento Distribuído

### 3.1. Configuração do Rastreamento com Spring Cloud Sleuth e Zipkin

Adicione as dependências no `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
```

Configure no `application.properties`:

```properties
spring.application.name=manager-user-security
spring.sleuth.sampler.probability=1.0
spring.zipkin.baseUrl=http://zipkin:9411
```

Adicione o Zipkin ao docker-compose:

```yaml
# Trecho do docker-compose.yml para Zipkin
zipkin:
  image: openzipkin/zipkin
  container_name: zipkin
  ports:
    - "9411:9411"
  networks:
    - manager-network
```

### 3.2. Rastreamento Personalizado

Para adicionar rastreamento personalizado em métodos específicos:

```java
@Component
public class SecurityAuditAspect {
    
    private final Tracer tracer;
    
    public SecurityAuditAspect(Tracer tracer) {
        this.tracer = tracer;
    }
    
    @Around("execution(* com.ocoelhogabriel.manager_user_security.application.services.*.*(..)) " +
            "|| execution(* com.ocoelhogabriel.manager_user_security.domain.services.*.*(..))")
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        
        Span span = tracer.currentSpan().name("method." + methodName);
        span.tag("class", joinPoint.getTarget().getClass().getSimpleName());
        
        try (Tracer.SpanInScope ws = tracer.withSpan(span.start())) {
            return joinPoint.proceed();
        } catch (Exception e) {
            span.tag("error", e.getClass().getSimpleName());
            throw e;
        } finally {
            span.finish();
        }
    }
}
```

## 4. Alertas

### 4.1. Configuração de Alertas no Prometheus

Crie um arquivo de regras de alertas (`prometheus/alert_rules.yml`):

```yaml
groups:
  - name: manager-user-security
    rules:
      # Alerta para alta taxa de erros HTTP
      - alert: HighErrorRate
        expr: sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m])) / sum(rate(http_server_requests_seconds_count[5m])) > 0.05
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "Alta taxa de erros HTTP"
          description: "Mais de 5% das requisições HTTP estão retornando erro 5xx"
          
      # Alerta para tempo de resposta lento
      - alert: SlowResponseTime
        expr: histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket{job="manager-user-security"}[5m])) by (le)) > 2
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Tempo de resposta lento"
          description: "95% das requisições estão levando mais de 2 segundos para processar"
          
      # Alerta para uso elevado de memória JVM
      - alert: HighMemoryUsage
        expr: sum(jvm_memory_used_bytes{job="manager-user-security"}) / sum(jvm_memory_max_bytes{job="manager-user-security"}) > 0.85
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Uso elevado de memória JVM"
          description: "A aplicação está usando mais de 85% da memória JVM disponível"
```

### 4.2. Configuração do Alertmanager

Configure o Alertmanager para notificações (`alertmanager/config.yml`):

```yaml
global:
  resolve_timeout: 5m

route:
  group_by: ['alertname', 'job']
  group_wait: 30s
  group_interval: 5m
  repeat_interval: 12h
  receiver: 'email-notifications'
  routes:
  - match:
      severity: critical
    receiver: 'slack-critical'
    continue: true

receivers:
- name: 'email-notifications'
  email_configs:
  - to: '${ALERT_EMAIL_TO}'
    from: '${ALERT_EMAIL_FROM}'
    smarthost: '${SMTP_SERVER}:${SMTP_PORT}'
    auth_username: '${SMTP_USERNAME}'
    auth_password: '${SMTP_PASSWORD}'
    
- name: 'slack-critical'
  slack_configs:
  - api_url: '${SLACK_WEBHOOK_URL}'
    channel: '#alerts-critical'
    title: "{{ .GroupLabels.alertname }}"
    text: "{{ .CommonAnnotations.description }}"
    send_resolved: true
```

Adicione o Alertmanager ao docker-compose:

```yaml
# Trecho do docker-compose.yml para Alertmanager
alertmanager:
  image: prom/alertmanager:latest
  container_name: alertmanager
  volumes:
    - ./alertmanager:/etc/alertmanager
  command: --config.file=/etc/alertmanager/config.yml
  ports:
    - "9093:9093"
  networks:
    - manager-network
```

## 5. Dashboards e Visualizações

### 5.1. Configuração do Grafana

```yaml
# Trecho do docker-compose.yml para Grafana
grafana:
  image: grafana/grafana:latest
  container_name: grafana
  volumes:
    - grafana-data:/var/lib/grafana
    - ./grafana/provisioning:/etc/grafana/provisioning
  environment:
    - GF_SECURITY_ADMIN_PASSWORD=${GRAFANA_ADMIN_PASSWORD}
    - GF_SECURITY_ADMIN_USER=${GRAFANA_ADMIN_USER}
    - GF_USERS_ALLOW_SIGN_UP=false
  ports:
    - "3000:3000"
  networks:
    - manager-network
```

### 5.2. Dashboards Recomendados

Criar os seguintes dashboards no Grafana:

1. **Dashboard Geral de Aplicação**:
   - Métricas JVM (memória, threads, garbage collection)
   - Requisições HTTP por segundo
   - Latência de requisições HTTP (percentis 50, 95, 99)
   - Taxa de erros

2. **Dashboard de Segurança**:
   - Tentativas de login (sucesso vs. falha)
   - Criação de usuários
   - Alterações de permissões
   - Eventos de segurança (login, logout, alterações de credenciais)

3. **Dashboard de Banco de Dados**:
   - Uso do pool de conexões
   - Tempos de execução de consultas
   - Consultas lentas
   - Métricas de transações

### 5.3. Exportação de Dashboards

Os dashboards do Grafana podem ser exportados como JSON e armazenados no repositório em `docs/operations/dashboards/`.

## 6. Arquitetura de Monitoramento Completa

```
+---------------+     +---------------+     +---------------+
|               |     |               |     |               |
| Application   |---->|  Prometheus   |---->|    Grafana    |
|               |     |               |     |               |
+-------+-------+     +-------+-------+     +---------------+
        |                     |
        v                     v
+-------+-------+     +-------+-------+     +---------------+
|               |     |               |     |               |
|  Logstash/    |---->| Elasticsearch |---->|    Kibana     |
|  Fluentd      |     |               |     |               |
+-------+-------+     +---------------+     +---------------+
        |
        v
+-------+-------+
|               |
|    Zipkin     |
|               |
+---------------+
```

## 7. Health Checks e Readiness Probes

### 7.1. Spring Boot Actuator

O Spring Boot Actuator fornece endpoints para verificar a saúde da aplicação:

- `/actuator/health`: Estado geral da saúde da aplicação
- `/actuator/health/liveness`: Verificação se a aplicação está viva
- `/actuator/health/readiness`: Verificação se a aplicação está pronta para receber tráfego

Configuração no `application.properties`:

```properties
management.endpoints.web.exposure.include=health,info,prometheus
management.endpoint.health.show-details=always
management.endpoint.health.probes.enabled=true
management.health.livenessState.enabled=true
management.health.readinessState.enabled=true
```

### 7.2. Health Indicators Personalizados

Crie indicadores de saúde personalizados:

```java
@Component
public class SecurityServicesHealthIndicator implements HealthIndicator {
    
    private final JwtTokenService tokenService;
    
    public SecurityServicesHealthIndicator(JwtTokenService tokenService) {
        this.tokenService = tokenService;
    }
    
    @Override
    public Health health() {
        try {
            // Realizar verificação de saúde do serviço de token
            if (tokenService.isOperational()) {
                return Health.up()
                    .withDetail("service", "JWT Token Service")
                    .withDetail("status", "operational")
                    .build();
            } else {
                return Health.down()
                    .withDetail("service", "JWT Token Service")
                    .withDetail("status", "not operational")
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withDetail("service", "JWT Token Service")
                .withDetail("exception", e.getMessage())
                .build();
        }
    }
}
```

## 8. Monitoramento em Produção: Checklist

✅ Métricas básicas expostas via Actuator/Prometheus
✅ Sistema de logs configurado e centralizado
✅ Rastreamento distribuído implementado
✅ Alertas configurados para eventos críticos
✅ Dashboards criados para visualização
✅ Health checks implementados
✅ Plano de resposta a incidentes documentado
✅ Equipe treinada para interpretar métricas e logs
✅ Procedimentos de escalação definidos

## Referências

- [Spring Boot Actuator Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Micrometer Documentation](https://micrometer.io/docs)
- [Prometheus Documentation](https://prometheus.io/docs/introduction/overview/)
- [Grafana Documentation](https://grafana.com/docs/grafana/latest/)
- [Elastic Stack Documentation](https://www.elastic.co/guide/index.html)
- [Spring Cloud Sleuth Documentation](https://spring.io/projects/spring-cloud-sleuth)