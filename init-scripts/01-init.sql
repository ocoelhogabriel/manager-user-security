-- Script inicial para criação de extensões e configurações
-- Executado durante a inicialização do PostgreSQL

-- Habilita extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_stat_statements";

-- Configurações de segurança
ALTER SYSTEM SET log_connections = on;
ALTER SYSTEM SET log_disconnections = on;
ALTER SYSTEM SET log_statement = 'ddl';
ALTER SYSTEM SET log_min_duration_statement = 500;
ALTER SYSTEM SET track_activity_query_size = 4096;

-- Configurações para auditoria
CREATE SCHEMA IF NOT EXISTS audit;
COMMENT ON SCHEMA audit IS 'Esquema para tabelas de auditoria';

-- Função para registrar alterações nas tabelas principais
CREATE OR REPLACE FUNCTION audit.audit_trigger_function()
RETURNS TRIGGER AS $$
DECLARE
    v_old_data jsonb;
    v_new_data jsonb;
BEGIN
    IF (TG_OP = 'DELETE') THEN
        v_old_data = to_jsonb(OLD);
        INSERT INTO audit.log_changes
        (table_name, operation_type, data_before, data_after, changed_by, ip_address)
        VALUES (TG_TABLE_NAME, TG_OP, v_old_data, null, current_user, inet_client_addr());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        v_old_data = to_jsonb(OLD);
        v_new_data = to_jsonb(NEW);
        INSERT INTO audit.log_changes
        (table_name, operation_type, data_before, data_after, changed_by, ip_address)
        VALUES (TG_TABLE_NAME, TG_OP, v_old_data, v_new_data, current_user, inet_client_addr());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        v_new_data = to_jsonb(NEW);
        INSERT INTO audit.log_changes
        (table_name, operation_type, data_before, data_after, changed_by, ip_address)
        VALUES (TG_TABLE_NAME, TG_OP, null, v_new_data, current_user, inet_client_addr());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Tabela de auditoria
CREATE TABLE IF NOT EXISTS audit.log_changes (
    id SERIAL PRIMARY KEY,
    table_name TEXT NOT NULL,
    operation_type TEXT NOT NULL,
    data_before JSONB,
    data_after JSONB,
    changed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    changed_by TEXT,
    ip_address INET
);

-- Índices para melhor performance nas consultas de auditoria
CREATE INDEX IF NOT EXISTS idx_log_changes_table_name ON audit.log_changes(table_name);
CREATE INDEX IF NOT EXISTS idx_log_changes_operation_type ON audit.log_changes(operation_type);
CREATE INDEX IF NOT EXISTS idx_log_changes_changed_at ON audit.log_changes(changed_at);
CREATE INDEX IF NOT EXISTS idx_log_changes_changed_by ON audit.log_changes(changed_by);

-- Comentário na tabela de auditoria
COMMENT ON TABLE audit.log_changes IS 'Tabela de auditoria para registrar alterações em tabelas principais';