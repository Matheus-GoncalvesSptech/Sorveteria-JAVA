# 🍦 Sorveteria

Projeto acadêmico em Java para gerenciar os produtos de uma sorveteria. O sistema roda no terminal (CLI com menu) e guarda os dados em um banco MySQL, usando JDBC.

## Sobre o projeto

A sorveteria vende vários tipos de produtos (casquinhas, picolés, bebidas, etc.), e entre eles estão os **sorvetes**, que têm informações extras como sabor, tamanho e cobertura. Para representar isso, o projeto usa **herança**: `Sorvete` é uma subclasse de `Produto`.

## Funcionalidades

- Cadastrar um produto comum (nome, preço e estoque)
- Cadastrar um sorvete (dados do produto + sabor, tamanho e cobertura)
- Listar todos os produtos cadastrados
- Buscar produtos pelo nome (ou parte dele)

## Tecnologias utilizadas

- **Java 17**
- **MySQL** (banco de dados)
- **JDBC** (conexão com o banco, driver `mysql-connector-j`)
- **Maven** (gerenciamento de dependências)

## Estrutura do projeto

```
sorveteria/
├── sql/
│   └── script.sql                 # criação do banco e das tabelas
├── src/main/java/sorveteria/
│   ├── Main.java                  # menu CLI
│   ├── model/
│   │   ├── Produto.java           # superclasse
│   │   └── Sorvete.java           # subclasse (herda de Produto)
│   ├── dao/
│   │   └── ProdutoDAO.java        # cadastro, listagem e busca no banco
│   └── db/
│       └── Conexao.java           # conexão JDBC com o MySQL
└── pom.xml
```

## Herança

- **`Produto`**: `id`, `nome`, `preco`, `estoque`
- **`Sorvete`** (herda de `Produto`): adiciona `sabor`, `tamanho` e `cobertura`

A classe `Sorvete` sobrescreve `getTipo()` e `toString()` para mostrar seus atributos extras. Na listagem, o `ProdutoDAO` devolve um `Sorvete` quando o produto tem dados na tabela `sorvete` e um `Produto` comum nos outros casos.

## Banco de dados

A herança foi representada com **uma tabela por classe**:

| Tabela    | Colunas                                                        |
|-----------|----------------------------------------------------------------|
| `produto` | `id` (PK, auto incremento), `nome`, `preco`, `estoque`         |
| `sorvete` | `id_produto` (PK e FK para `produto.id`), `sabor`, `tamanho`, `cobertura` |

Ao cadastrar um sorvete, os dados são gravados nas duas tabelas dentro da mesma transação: se uma gravação falhar, nada é salvo.

## Diagrama de classes

<!-- Cole aqui o seu diagrama (imagem ou bloco Mermaid) -->

## Como executar

**Pré-requisitos:** Java 17+, Maven e MySQL instalados.

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   cd sorveteria
   ```
2. Crie o banco executando o script no MySQL:
   ```bash
   mysql -u root -p < sql/script.sql
   ```
3. Em `src/main/java/sorveteria/db/Conexao.java`, ajuste o usuário e a senha do seu MySQL.
4. Compile e execute (pela IDE, rodando a classe `Main`, ou pelo terminal):
   ```bash
   mvn compile exec:java -Dexec.mainClass="sorveteria.Main"
   ```

## Exemplo de uso

```
===== SORVETERIA =====
1 - Cadastrar produto
2 - Cadastrar sorvete
3 - Listar produtos
4 - Buscar produto por nome
0 - Sair
```

Exemplo de listagem:

```
[1] Produto | Casquinha | R$ 3.00 | estoque: 50
[2] Sorvete | Sundae | R$ 12.00 | estoque: 20 | sabor: Chocolate | tamanho: M | cobertura: Calda de morango
```

## Autor

Matheus Gonçalves, estudante de Sistemas de Informação na São Paulo Tech School.
