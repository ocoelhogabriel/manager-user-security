# Guia de Deployment - Manager User Security

Este guia descreve o processo de deployment do sistema Manager User Security em diferentes ambientes.

## Pré-requisitos

- JDK 17 ou superior
- PostgreSQL 14 ou superior
- Docker e Docker Compose (para deployment containerizado)
- Servidor com pelo menos 2GB de RAM e 2 vCPUs
- Sistema operacional Linux (recomendado: Ubuntu 22.04 LTS)

## Opções de Deployment

O sistema suporta várias estratégias de deployment, dependendo das necessidades do ambiente:

1. **Deployment Tradicional** - JAR executável em servidor Java
2. **Deployment Containerizado** - Usando Docker e Docker Compose
3. **Deployment em Kubernetes** - Para ambientes de alta disponibilidade

## 1. Deployment Tradicional

### 1.1. Preparação do Ambiente

```bash
# Instalar JDK
sudo apt update
sudo apt install openjdk-17-jdk

# Verificar instalação
java -version

# Criar diretório da aplicação
sudo mkdir -p /opt/manager-user-security
sudo chown -R $USER:$USER /opt/manager-user-security
```

### 1.2. Configuração do Banco de Dados

```bash
# Instalar PostgreSQL
sudo apt install postgresql postgresql-contrib

# Iniciar PostgreSQL
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Criar usuário e banco de dados
sudo -u postgres psql -c "CREATE USER manager_security WITH PASSWORD 'secure_password';"
sudo -u postgres psql -c "CREATE DATABASE manager_user_security;"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE manager_user_security TO manager_security;"
```

### 1.3. Deploy da Aplicação

```bash
# Copiar o JAR para o servidor
scp target/manager-user-security.jar user@server:/opt/manager-user-security/

# Criar arquivo de configuração
cat > /opt/manager-user-security/application.properties << EOF
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/manager_user_security
spring.datasource.username=manager_security
spring.datasource.password=secure_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=8080
server.servlet.context-path=/manager_user_security

# JWT Configuration
jwt.secret=your_secure_jwt_secret_key_here
jwt.expiration=86400000
EOF

# Criar script de inicialização
cat > /opt/manager-user-security/start.sh << EOF
#!/bin/bash
java -jar manager-user-security.jar --spring.config.location=file:./application.properties
EOF

chmod +x /opt/manager-user-security/start.sh
```

### 1.4. Configuração de Serviço Systemd

```bash
cat > /etc/systemd/system/manager-user-security.service << EOF
[Unit]
Description=Manager User Security Service
After=network.target postgresql.service

[Service]
User=app_user
WorkingDirectory=/opt/manager-user-security
ExecStart=/opt/manager-user-security/start.sh
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

# Recarregar systemd
sudo systemctl daemon-reload

# Iniciar o serviço
sudo systemctl start manager-user-security

# Habilitar inicialização automática
sudo systemctl enable manager-user-security

# Verificar status
sudo systemctl status manager-user-security
```

## 2. Deployment Containerizado com Docker Compose

### 2.1. Preparação do Ambiente

```bash
# Instalar Docker
sudo apt update
sudo apt install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt update
sudo apt install docker-ce

# Instalar Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Verificar instalações
docker --version
docker-compose --version

# Adicionar usuário ao grupo docker
sudo usermod -aG docker $USER
```

### 2.2. Configuração do Deployment

Crie um diretório para o projeto:

```bash
mkdir -p /opt/manager-user-security
cd /opt/manager-user-security
```

Crie um arquivo `.env` para as variáveis de ambiente:

```bash
cat > .env << EOF
POSTGRES_DB=manager_user_security
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres_password
POSTGRES_HOST=postgres
POSTGRES_PORT=5432
JWT_SECRET=your_secure_jwt_secret_key_here
JWT_EXPIRATION=86400000
EOF
```

### 2.3. Docker Compose

Crie o arquivo `docker-compose.yml`:

```bash
cat > docker-compose.yml << EOF
version: '3.8'

services:
  backend:
    image: ocoelhogabriel/manager-user-security:latest
    container_name: manager-user-security
    restart: unless-stopped
    depends_on:
      - postgres
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/\${POSTGRES_DB}
      - SPRING_DATASOURCE_USERNAME=\${POSTGRES_USER}
      - SPRING_DATASOURCE_PASSWORD=\${POSTGRES_PASSWORD}
      - SPRING_JPA_HIBERNATE_DDL_AUTO=validate
      - JWT_SECRET=\${JWT_SECRET}
      - JWT_EXPIRATION=\${JWT_EXPIRATION}
    ports:
      - "8080:8080"
    networks:
      - manager-network
    volumes:
      - ./logs:/app/logs
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/manager_user_security/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  postgres:
    image: postgres:14-alpine
    container_name: manager-postgres
    restart: unless-stopped
    environment:
      - POSTGRES_DB=\${POSTGRES_DB}
      - POSTGRES_USER=\${POSTGRES_USER}
      - POSTGRES_PASSWORD=\${POSTGRES_PASSWORD}
    volumes:
      - postgres-data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    networks:
      - manager-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U \${POSTGRES_USER} -d \${POSTGRES_DB}"]
      interval: 10s
      timeout: 5s
      retries: 5

networks:
  manager-network:
    driver: bridge

volumes:
  postgres-data:
EOF
```

### 2.4. Execução do Deployment

```bash
# Iniciar os serviços
docker-compose up -d

# Verificar logs
docker-compose logs -f

# Verificar status
docker-compose ps
```

## 3. Deployment em Kubernetes

### 3.1. Pré-requisitos

- Cluster Kubernetes em funcionamento
- kubectl configurado para o cluster
- Helm (opcional, para instalação do PostgreSQL)

### 3.2. Configuração do Kubernetes

Crie um namespace para a aplicação:

```bash
kubectl create namespace manager-user-security
```

Crie um secret para as credenciais do banco de dados e JWT:

```bash
kubectl create secret generic manager-user-security-secret \
  --from-literal=postgres-password=secure_password \
  --from-literal=jwt-secret=your_secure_jwt_secret_key_here \
  -n manager-user-security
```

### 3.3. Deployment do PostgreSQL

Usando Helm (método recomendado):

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install postgres bitnami/postgresql \
  --set auth.username=manager_security \
  --set auth.password=secure_password \
  --set auth.database=manager_user_security \
  --set persistence.size=10Gi \
  -n manager-user-security
```

Ou crie manifestos para o PostgreSQL:

```bash
cat > postgres-deployment.yaml << EOF
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: postgres
  namespace: manager-user-security
spec:
  serviceName: postgres
  replicas: 1
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
    spec:
      containers:
      - name: postgres
        image: postgres:14
        ports:
        - containerPort: 5432
        env:
        - name: POSTGRES_DB
          value: manager_user_security
        - name: POSTGRES_USER
          value: manager_security
        - name: POSTGRES_PASSWORD
          valueFrom:
            secretKeyRef:
              name: manager-user-security-secret
              key: postgres-password
        volumeMounts:
        - name: postgres-data
          mountPath: /var/lib/postgresql/data
  volumeClaimTemplates:
  - metadata:
      name: postgres-data
    spec:
      accessModes: [ "ReadWriteOnce" ]
      resources:
        requests:
          storage: 10Gi
---
apiVersion: v1
kind: Service
metadata:
  name: postgres
  namespace: manager-user-security
spec:
  selector:
    app: postgres
  ports:
  - port: 5432
    targetPort: 5432
  type: ClusterIP
EOF

kubectl apply -f postgres-deployment.yaml
```

### 3.4. Deployment da Aplicação

Crie o arquivo de deployment da aplicação:

```bash
cat > app-deployment.yaml << EOF
apiVersion: apps/v1
kind: Deployment
metadata:
  name: manager-user-security
  namespace: manager-user-security
spec:
  replicas: 2
  selector:
    matchLabels:
      app: manager-user-security
  template:
    metadata:
      labels:
        app: manager-user-security
    spec:
      containers:
      - name: manager-user-security
        image: ocoelhogabriel/manager-user-security:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_DATASOURCE_URL
          value: jdbc:postgresql://postgres:5432/manager_user_security
        - name: SPRING_DATASOURCE_USERNAME
          value: manager_security
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: manager-user-security-secret
              key: postgres-password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: manager-user-security-secret
              key: jwt-secret
        - name: JWT_EXPIRATION
          value: "86400000"
        readinessProbe:
          httpGet:
            path: /manager_user_security/actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        livenessProbe:
          httpGet:
            path: /manager_user_security/actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        resources:
          limits:
            cpu: "1"
            memory: "1Gi"
          requests:
            cpu: "500m"
            memory: "512Mi"
---
apiVersion: v1
kind: Service
metadata:
  name: manager-user-security
  namespace: manager-user-security
spec:
  selector:
    app: manager-user-security
  ports:
  - port: 80
    targetPort: 8080
  type: ClusterIP
---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: manager-user-security
  namespace: manager-user-security
  annotations:
    kubernetes.io/ingress.class: nginx
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
spec:
  rules:
  - host: manager-security.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: manager-user-security
            port:
              number: 80
  tls:
  - hosts:
    - manager-security.example.com
    secretName: manager-security-tls
EOF

kubectl apply -f app-deployment.yaml
```

### 3.5. Verificação do Deployment

```bash
# Verificar pods
kubectl get pods -n manager-user-security

# Verificar serviços
kubectl get svc -n manager-user-security

# Verificar logs
kubectl logs -l app=manager-user-security -n manager-user-security

# Verificar ingress
kubectl get ingress -n manager-user-security
```

## 4. Monitoramento e Observabilidade

Recomenda-se configurar ferramentas de monitoramento para acompanhar a saúde da aplicação:

- Prometheus para coleta de métricas
- Grafana para visualização
- ELK Stack ou Loki para gerenciamento de logs
- Jaeger ou Zipkin para rastreamento distribuído

## 5. Backup e Recuperação

### 5.1. Backup do Banco de Dados

```bash
# Para PostgreSQL local
pg_dump -U manager_security -d manager_user_security > backup_$(date +%Y%m%d).sql

# Para PostgreSQL em Docker
docker exec manager-postgres pg_dump -U postgres -d manager_user_security > backup_$(date +%Y%m%d).sql

# Para PostgreSQL em Kubernetes
kubectl exec -it postgres-0 -n manager-user-security -- pg_dump -U manager_security -d manager_user_security > backup_$(date +%Y%m%d).sql
```

### 5.2. Recuperação do Banco de Dados

```bash
# Para PostgreSQL local
psql -U manager_security -d manager_user_security < backup_YYYYMMDD.sql

# Para PostgreSQL em Docker
cat backup_YYYYMMDD.sql | docker exec -i manager-postgres psql -U postgres -d manager_user_security

# Para PostgreSQL em Kubernetes
cat backup_YYYYMMDD.sql | kubectl exec -i postgres-0 -n manager-user-security -- psql -U manager_security -d manager_user_security
```

## 6. Considerações de Segurança

1. **Secrets**: Sempre use secrets para armazenar senhas e tokens
2. **TLS**: Configure TLS para todas as comunicações externas
3. **Network Policies**: No Kubernetes, use network policies para restringir o tráfego
4. **Security Context**: Configure security context para limitar privilégios
5. **Updates**: Mantenha imagens de containers e dependências atualizadas

## 7. Troubleshooting

### 7.1. Problemas Comuns

#### Aplicação não inicia

Verifique:
- Logs da aplicação
- Conexão com o banco de dados
- Configurações de ambiente corretas

#### Falhas de Conexão com Banco de Dados

Verifique:
- Se o serviço de banco de dados está em execução
- Credenciais corretas
- Portas e firewalls

#### Problemas de Performance

Verifique:
- Uso de CPU e memória
- Conexões de banco de dados
- Timeouts

### 7.2. Comandos Úteis para Diagnóstico

```bash
# Ver logs da aplicação
tail -f /opt/manager-user-security/logs/application.log

# Em Docker
docker logs manager-user-security

# Em Kubernetes
kubectl logs -l app=manager-user-security -n manager-user-security

# Verificar conectividade com banco de dados
psql -h localhost -U manager_security -d manager_user_security -c "SELECT 1"

# Verificar uso de recursos
top
htop
docker stats
kubectl top pods -n manager-user-security
```

## 8. Atualizações e Rollbacks

### 8.1. Atualizações

```bash
# Deployment tradicional
cd /opt/manager-user-security
cp manager-user-security.jar manager-user-security.jar.bak
scp new-version.jar user@server:/opt/manager-user-security/manager-user-security.jar
sudo systemctl restart manager-user-security

# Docker
docker-compose pull
docker-compose up -d

# Kubernetes
kubectl set image deployment/manager-user-security manager-user-security=ocoelhogabriel/manager-user-security:new-version -n manager-user-security
```

### 8.2. Rollbacks

```bash
# Deployment tradicional
cd /opt/manager-user-security
mv manager-user-security.jar.bak manager-user-security.jar
sudo systemctl restart manager-user-security

# Docker
docker-compose down
docker tag ocoelhogabriel/manager-user-security:previous ocoelhogabriel/manager-user-security:latest
docker-compose up -d

# Kubernetes
kubectl rollout undo deployment/manager-user-security -n manager-user-security
```

## Referências

- [Spring Boot Deployment Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html)
- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/home/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
