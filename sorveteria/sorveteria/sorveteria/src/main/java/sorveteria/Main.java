package sorveteria;

import sorveteria.dao.ClienteDAO;
import sorveteria.dao.PedidoDAO;
import sorveteria.dao.ProdutoDAO;
import sorveteria.model.Cliente;
import sorveteria.model.Pedido;
import sorveteria.model.Picole;
import sorveteria.model.Produto;
import sorveteria.model.Sorvete;
import sorveteria.model.Sorveteria;
import sorveteria.model.Tamanho;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProdutoDAO produtoDAO = new ProdutoDAO();
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenu();
            opcao = lerInt("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> cadastrarSorvete();
                case 2 -> cadastrarPicole();
                case 3 -> listarProdutos();
                case 4 -> buscarProdutos();
                case 5 -> cadastrarCliente();
                case 6 -> listarClientes();
                case 7 -> registrarPedido();
                case 8 -> listarPedidos();
                case 9 -> buscarPedidosPorCpf();
                case 10 -> mostrarFaturamento();
                case 11 -> mostrarTotalCliente();
                case 0 -> System.out.println("Saindo... Até logo!");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("===== SORVETERIA =====");
        System.out.println("1  - Cadastrar sorvete");
        System.out.println("2  - Cadastrar picolé");
        System.out.println("3  - Listar produtos");
        System.out.println("4  - Buscar produto por nome");
        System.out.println("5  - Cadastrar cliente");
        System.out.println("6  - Listar clientes");
        System.out.println("7  - Registrar pedido");
        System.out.println("8  - Listar pedidos");
        System.out.println("9  - Buscar pedidos por CPF");
        System.out.println("10 - Faturamento total da sorveteria");
        System.out.println("11 - Total gasto por cliente (CPF)");
        System.out.println("0  - Sair");
    }

    // ---------- produtos ----------

    private static void cadastrarSorvete() {
        System.out.println("\n--- Novo sorvete ---");
        String nome = lerTexto("Nome: ");
        double precoBase = lerDouble("Preço base (tamanho pequeno): ");
        String sabor = lerTexto("Sabor: ");
        Tamanho tamanho = lerTamanho();

        try {
            produtoDAO.inserir(new Sorvete(nome, precoBase, sabor, tamanho));
            System.out.println("Sorvete cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static void cadastrarPicole() {
        System.out.println("\n--- Novo picolé ---");
        String nome = lerTexto("Nome: ");
        double precoBase = lerDouble("Preço base: ");
        boolean temCobertura = lerSimNao("Tem cobertura? (s/n): ");

        try {
            produtoDAO.inserir(new Picole(nome, precoBase, temCobertura));
            System.out.println("Picolé cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static void listarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listar();
            System.out.println("\n--- Produtos cadastrados ---");
            imprimir(produtos, "Nenhum produto encontrado.");
        } catch (SQLException e) {
            System.out.println("Erro ao listar: " + e.getMessage());
        }
    }

    private static void buscarProdutos() {
        String termo = lerTexto("\nNome (ou parte dele): ");
        try {
            List<Produto> produtos = produtoDAO.buscarPorNome(termo);
            System.out.println("\n--- Resultado da busca ---");
            imprimir(produtos, "Nenhum produto encontrado.");
        } catch (SQLException e) {
            System.out.println("Erro ao buscar: " + e.getMessage());
        }
    }

    // ---------- clientes ----------

    private static void cadastrarCliente() {
        System.out.println("\n--- Novo cliente ---");
        String nome = lerTexto("Nome: ");
        String cpf = lerCpf("CPF (11 dígitos): ");
        String email = lerEmail("E-mail: ");
        LocalDate nascimento = lerData("Data de nascimento (dd/MM/yyyy): ");

        try {
            if (clienteDAO.buscarPorCpf(cpf) != null) {
                System.out.println("Já existe um cliente com esse CPF.");
                return;
            }
            clienteDAO.inserir(new Cliente(nome, cpf, email, nascimento));
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static void listarClientes() {
        try {
            List<Cliente> clientes = clienteDAO.listar();
            System.out.println("\n--- Clientes cadastrados ---");
            imprimir(clientes, "Nenhum cliente encontrado.");
        } catch (SQLException e) {
            System.out.println("Erro ao listar: " + e.getMessage());
        }
    }

    // ---------- pedidos ----------

    private static void registrarPedido() {
        try {
            List<Produto> produtos = produtoDAO.listar();
            if (produtos.isEmpty()) {
                System.out.println("Cadastre um produto antes de registrar um pedido.");
                return;
            }
            System.out.println("\n--- Novo pedido ---");
            imprimir(produtos, "");

            Produto produto = produtoDAO.buscarPorId(lerInt("\nId do produto: "));
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            Cliente cliente = clienteDAO.buscarPorCpf(lerCpf("CPF do cliente: "));
            if (cliente == null) {
                System.out.println("Cliente não encontrado. Cadastre-o antes (opção 5).");
                return;
            }

            int quantidade = lerIntPositivo("Quantidade: ");
            Pedido pedido = new Pedido(cliente, produto, quantidade, LocalDate.now());

            pedidoDAO.inserir(pedido);
            System.out.println("\nPedido registrado com sucesso!");
            System.out.println("Subtotal: " + moeda(pedido.calcularSubtotal()));
            System.out.println("Desconto: " + moeda(pedido.calcularDesconto()));
            System.out.println("Total:    " + moeda(pedido.calcularTotal()));
        } catch (SQLException e) {
            System.out.println("Erro ao registrar pedido: " + e.getMessage());
        }
    }

    private static void listarPedidos() {
        try {
            List<Pedido> pedidos = pedidoDAO.listar();
            System.out.println("\n--- Pedidos ---");
            imprimir(pedidos, "Nenhum pedido encontrado.");
        } catch (SQLException e) {
            System.out.println("Erro ao listar: " + e.getMessage());
        }
    }

    private static void buscarPedidosPorCpf() {
        String cpf = lerCpf("\nCPF do cliente: ");
        try {
            Sorveteria sorveteria = carregarSorveteria();
            System.out.println("\n--- Pedidos do CPF " + cpf + " ---");
            imprimir(sorveteria.buscarPorCpf(cpf), "Nenhum pedido encontrado para esse CPF.");
        } catch (SQLException e) {
            System.out.println("Erro ao buscar: " + e.getMessage());
        }
    }

    // ---------- relatórios ----------

    private static void mostrarFaturamento() {
        try {
            Sorveteria sorveteria = carregarSorveteria();
            System.out.println("\nFaturamento total da " + sorveteria.getNome() + ": "
                    + moeda(sorveteria.calcularFaturamentoTotal()));
        } catch (SQLException e) {
            System.out.println("Erro ao calcular: " + e.getMessage());
        }
    }

    private static void mostrarTotalCliente() {
        String cpf = lerCpf("\nCPF do cliente: ");
        try {
            Sorveteria sorveteria = carregarSorveteria();
            System.out.println("Total gasto pelo CPF " + cpf + ": "
                    + moeda(sorveteria.calcularTotalCliente(cpf)));
        } catch (SQLException e) {
            System.out.println("Erro ao calcular: " + e.getMessage());
        }
    }

    // Monta o objeto Sorveteria com os pedidos que estão no banco
    private static Sorveteria carregarSorveteria() throws SQLException {
        Sorveteria sorveteria = new Sorveteria("Sorveteria Gelato", "12.345.678/0001-90");
        for (Pedido pedido : pedidoDAO.listar()) {
            sorveteria.adicionarPedido(pedido);
        }
        return sorveteria;
    }

    private static <T> void imprimir(List<T> itens, String mensagemVazia) {
        if (itens.isEmpty()) {
            if (!mensagemVazia.isEmpty()) {
                System.out.println(mensagemVazia);
            }
            return;
        }
        for (T item : itens) {
            System.out.println(item);
        }
    }

    private static String moeda(double valor) {
        return String.format("R$ %.2f", valor);
    }

    // ---------- leitura segura do teclado ----------

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private static int lerInt(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(mensagem));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static int lerIntPositivo(String mensagem) {
        while (true) {
            int valor = lerInt(mensagem);
            if (valor > 0) {
                return valor;
            }
            System.out.println("O valor deve ser maior que zero.");
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            try {
                double valor = Double.parseDouble(lerTexto(mensagem).replace(",", "."));
                if (valor > 0) {
                    return valor;
                }
                System.out.println("O valor deve ser maior que zero.");
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido (ex.: 7.50).");
            }
        }
    }

    private static boolean lerSimNao(String mensagem) {
        while (true) {
            String resposta = lerTexto(mensagem).toLowerCase();
            if (resposta.equals("s") || resposta.equals("sim")) {
                return true;
            }
            if (resposta.equals("n") || resposta.equals("nao") || resposta.equals("não")) {
                return false;
            }
            System.out.println("Responda com s ou n.");
        }
    }

    private static Tamanho lerTamanho() {
        while (true) {
            System.out.println("Tamanho: 1 - Pequeno | 2 - Médio | 3 - Grande");
            switch (lerInt("Escolha: ")) {
                case 1: return Tamanho.PEQUENO;
                case 2: return Tamanho.MEDIO;
                case 3: return Tamanho.GRANDE;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    // Aceita CPF com ou sem pontuação e guarda só os 11 dígitos
    private static String lerCpf(String mensagem) {
        while (true) {
            String cpf = lerTexto(mensagem).replaceAll("\\D", "");
            if (cpf.length() == 11) {
                return cpf;
            }
            System.out.println("O CPF deve ter 11 dígitos.");
        }
    }

    private static String lerEmail(String mensagem) {
        while (true) {
            String email = lerTexto(mensagem);
            if (email.contains("@") && email.contains(".")) {
                return email;
            }
            System.out.println("Digite um e-mail válido.");
        }
    }

    private static LocalDate lerData(String mensagem) {
        while (true) {
            try {
                LocalDate data = LocalDate.parse(lerTexto(mensagem), FORMATO_DATA);
                if (!data.isAfter(LocalDate.now())) {
                    return data;
                }
                System.out.println("A data não pode estar no futuro.");
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd/MM/yyyy.");
            }
        }
    }
}
