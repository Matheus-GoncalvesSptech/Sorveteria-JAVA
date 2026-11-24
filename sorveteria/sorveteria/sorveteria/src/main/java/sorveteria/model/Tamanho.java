package sorveteria.model;

public enum Tamanho {
    PEQUENO(1.0),
    MEDIO(1.5),
    GRANDE(2.0);

    private final double multiplicador;

    Tamanho(double multiplicador) {
        this.multiplicador = multiplicador;
    }

    public double getMultiplicador() {
        return multiplicador;
    }
}
