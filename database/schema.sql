-- ============================================================
-- Script de criacao do banco de dados - API Lavacao
-- SGBD: PostgreSQL
-- ============================================================

-- Cria o banco de dados (execute fora de uma transacao, se necessario)
-- CREATE DATABASE api_lavacao;

-- ============================================================
-- Tabela: cliente
-- ============================================================
CREATE TABLE cliente (
    id            SERIAL PRIMARY KEY,
    nome          VARCHAR(255) NOT NULL,
    celular       VARCHAR(255),
    email         VARCHAR(255),
    data_cadastro DATE
);

-- ============================================================
-- Tabela: cor
-- ============================================================
CREATE TABLE cor (
    id   SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

-- ============================================================
-- Tabela: marca
-- ============================================================
CREATE TABLE marca (
    id   SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

-- ============================================================
-- Tabela: modelo
-- ============================================================
CREATE TABLE modelo (
    id        SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    categoria VARCHAR(255),
    marca_id  INTEGER NOT NULL,
    CONSTRAINT fk_modelo_marca FOREIGN KEY (marca_id) REFERENCES marca (id)
);

-- ============================================================
-- Tabela: motor
-- ============================================================
CREATE TABLE motor (
    id               SERIAL PRIMARY KEY,
    potencia         INTEGER,
    tipo_combustivel VARCHAR(255)
);

-- ============================================================
-- Tabela: veiculo
-- ============================================================
CREATE TABLE veiculo (
    id              SERIAL PRIMARY KEY,
    placa           VARCHAR(255) NOT NULL,
    observacoes     VARCHAR(255),
    cor_id          INTEGER NOT NULL,
    modelo_id       INTEGER NOT NULL,
    proprietario_id INTEGER NOT NULL,
    CONSTRAINT fk_veiculo_cor          FOREIGN KEY (cor_id)          REFERENCES cor (id),
    CONSTRAINT fk_veiculo_modelo       FOREIGN KEY (modelo_id)       REFERENCES modelo (id),
    CONSTRAINT fk_veiculo_proprietario FOREIGN KEY (proprietario_id) REFERENCES cliente (id)
);

-- ============================================================
-- Dados de exemplo (opcional)
-- ============================================================
-- INSERT INTO marca (nome) VALUES ('Volkswagen');
-- INSERT INTO cor (nome) VALUES ('Prata');
-- INSERT INTO cliente (nome, celular, email, data_cadastro)
--     VALUES ('Joao da Silva', '48999999999', 'joao@email.com', '2026-10-20');
-- INSERT INTO modelo (descricao, categoria, marca_id) VALUES ('Gol', 'PADRAO', 1);
-- INSERT INTO veiculo (placa, observacoes, cor_id, modelo_id, proprietario_id)
--     VALUES ('ABC1D23', 'Sem observacoes', 1, 1, 1);
