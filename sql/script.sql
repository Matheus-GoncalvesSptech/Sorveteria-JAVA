-- Script de criação do banco da Sorveteria
CREATE DATABASE IF NOT EXISTS sorveteria;
USE sorveteria;

-- Superclasse: dados comuns a todo produto
CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100)  NOT NULL,
    preco_base DECIMAL(10,2) NOT NULL
);

-- Subclasse: atributos extras do sorvete
CREATE TABLE sorvete (
    id_produto INT PRIMARY KEY,
    sabor VARCHAR(50) NOT NULL,
    tamanho VARCHAR(20) NOT NULL,
    CONSTRAINT fk_sorvete_produto
        FOREIGN KEY (id_produto) REFERENCES produto(id)
        ON DELETE CASCADE
);

-- Subclasse: atributos extras do picolé (mesmo id do produto)
CREATE TABLE picole (
    id_produto INT PRIMARY KEY,
    tem_cobertura BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_picole_produto
        FOREIGN KEY (id_produto) REFERENCES produto(id)
        ON DELETE CASCADE
);

CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11)  NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL
);

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT  NOT NULL,
    id_produto INT  NOT NULL,
    quantidade INT  NOT NULL CHECK (quantidade > 0),
    data_pedido DATE NOT NULL,
    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente(id),
    CONSTRAINT fk_pedido_produto
        FOREIGN KEY (id_produto) REFERENCES produto(id)
);
