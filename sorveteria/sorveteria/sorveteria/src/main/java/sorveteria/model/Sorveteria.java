package sorveteria.model;

import java.util.ArrayList;
import java.util.List;

// Agrupa os pedidos da loja e calcula os totais (não tem tabela própria no banco)
public class Sorveteria {
    private String nome;
    private String cnpj;
    private final List<Pedido> pedidos = new ArrayList<>();

    public Sorveteria(String nome, String cnpj) {
        this.nome = nome;
        this.cnpj = cnpj;
    }

    public String getNome() { return nome; }
    public String getCnpj() { return cnpj; }

    public void adicionarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public List<Pedido> buscarPorCpf(String cpf) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getCliente().getCpf().equals(cpf)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public double calcularFaturamentoTotal() {
        double total = 0;
        for (Pedido p : pedidos) {
            total += p.calcularTotal();
        }
        return total;
    }

    public double calcularTotalCliente(String cpf) {
        double total = 0;
        for (Pedido p : buscarPorCpf(cpf)) {
            total += p.calcularTotal();
        }
        return total;
    }
}
