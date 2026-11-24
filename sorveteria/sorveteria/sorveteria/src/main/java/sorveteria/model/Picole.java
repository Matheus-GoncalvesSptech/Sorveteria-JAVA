package sorveteria.model;

public class Picole extends Produto {
    private static final double ADICIONAL_COBERTURA = 2.00;

    private boolean temCobertura;

    public Picole(String nome, double precoBase, boolean temCobertura) {
        super(nome, precoBase);
        this.temCobertura = temCobertura;
    }

    public Picole(int id, String nome, double precoBase, boolean temCobertura) {
        super(id, nome, precoBase);
        this.temCobertura = temCobertura;
    }

    public boolean isTemCobertura() { return temCobertura; }
    public void setTemCobertura(boolean temCobertura) { this.temCobertura = temCobertura; }

    // preço = (preço base + adicional da cobertura, se tiver) x quantidade
    @Override
    public double calcularPreco(int quantidade) {
        double unitario = getPrecoBase() + (temCobertura ? ADICIONAL_COBERTURA : 0);
        return unitario * quantidade;
    }

    @Override
    public String getTipo() {
        return "Picolé";
    }

    @Override
    public String toString() {
        return super.toString() + " | cobertura: " + (temCobertura ? "sim" : "não");
    }
}
