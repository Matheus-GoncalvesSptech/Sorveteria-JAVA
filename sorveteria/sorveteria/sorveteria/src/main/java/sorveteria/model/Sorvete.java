package sorveteria.model;

public class Sorvete extends Produto {
    private String sabor;
    private Tamanho tamanho;

    public Sorvete(String nome, double precoBase, String sabor, Tamanho tamanho) {
        super(nome, precoBase);
        this.sabor = sabor;
        this.tamanho = tamanho;
    }

    public Sorvete(int id, String nome, double precoBase, String sabor, Tamanho tamanho) {
        super(id, nome, precoBase);
        this.sabor = sabor;
        this.tamanho = tamanho;
    }

    public String getSabor() { return sabor; }
    public void setSabor(String sabor) { this.sabor = sabor; }

    public Tamanho getTamanho() { return tamanho; }
    public void setTamanho(Tamanho tamanho) { this.tamanho = tamanho; }

    // preço = preço base x multiplicador do tamanho x quantidade
    @Override
    public double calcularPreco(int quantidade) {
        return getPrecoBase() * tamanho.getMultiplicador() * quantidade;
    }

    @Override
    public String getTipo() {
        return "Sorvete";
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | sabor: %s | tamanho: %s", sabor, tamanho);
    }
}
