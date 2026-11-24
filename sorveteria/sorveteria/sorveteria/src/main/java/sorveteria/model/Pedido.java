package sorveteria.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pedido {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int id;
    private Cliente cliente;
    private Produto produto;
    private int quantidade;
    private LocalDate dataPedido;

    public Pedido(Cliente cliente, Produto produto, int quantidade, LocalDate dataPedido) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.cliente = cliente;
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataPedido = dataPedido;
    }

    public Pedido(int id, Cliente cliente, Produto produto, int quantidade, LocalDate dataPedido) {
        this(cliente, produto, quantidade, dataPedido);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public LocalDate getDataPedido() { return dataPedido; }

    public double calcularSubtotal() {
        return produto.calcularPreco(quantidade);
    }

    public double calcularDesconto() {
        return produto.calcularDesconto(quantidade);
    }

    public double calcularTotal() {
        return produto.calcularPrecoFinal(quantidade);
    }

    @Override
    public String toString() {
        return String.format("Pedido #%d | %s | %s (CPF %s) | %dx %s | subtotal: R$ %.2f | desconto: R$ %.2f | total: R$ %.2f",
                id, dataPedido.format(FORMATO), cliente.getNome(), cliente.getCpf(),
                quantidade, produto.getNome(), calcularSubtotal(), calcularDesconto(), calcularTotal());
    }
}
