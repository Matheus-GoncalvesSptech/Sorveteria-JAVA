-- Script de criação do banco da Sorveteria
CREATE DATABASE IF NOT EXISTS sorveteria;
USE sorveteria;

-- Superclasse: dados comuns a todo produto
CREATE TABLE IF NOT EXISTS produto (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nome    VARCHAR(100)   NOT NULL,
    preco   DECIMAL(10,2)  NOT NULL,
    estoque INT            NOT NULL DEFAULT 0
);

-- Subclasse: atributos extras do sorvete (mesmo id do produto)
CREATE TABLE IF NOT EXISTS sorvete (
    id_produto INT PRIMARY KEY,
    sabor      VARCHAR(50) NOT NULL,
    tamanho    VARCHAR(20) NOT NULL,
    cobertura  VARCHAR(50),
    CONSTRAINT fk_sorvete_produto
        FOREIGN KEY (id_produto) REFERENCES produto(id)
        ON DELETE CASCADE
);
