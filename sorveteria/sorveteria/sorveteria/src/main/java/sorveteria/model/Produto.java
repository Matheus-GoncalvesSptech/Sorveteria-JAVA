package sorveteria.model;

public abstract class Produto {
    // Regra de desconto: a partir de 5 unidades no mesmo pedido, 10% de desconto
    private static final int QUANTIDADE_PARA_DESCONTO = 5;
    private static final double PERCENTUAL_DESCONTO = 0.10;

    private int id;
    private String nome;
    private double precoBase;

    public Produto(String nome, double precoBase) {
        this.nome = nome;
        this.precoBase = precoBase;
    }

    public Produto(int id, String nome, double precoBase) {
        this(nome, precoBase);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPrecoBase() { return precoBase; }
    public void setPrecoBase(double precoBase) { this.precoBase = precoBase; }

    // Cada subclasse calcula o preço do seu jeito (polimorfismo)
    public abstract double calcularPreco(int quantidade);

    public abstract String getTipo();

    public double calcularDesconto(int quantidade) {
        if (quantidade >= QUANTIDADE_PARA_DESCONTO) {
            return calcularPreco(quantidade) * PERCENTUAL_DESCONTO;
        }
        return 0;
    }

    public double calcularPrecoFinal(int quantidade) {
        return calcularPreco(quantidade) - calcularDesconto(quantidade);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | preço base: R$ %.2f", id, getTipo(), nome, precoBase);
    }
}
