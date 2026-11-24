package sorveteria.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cliente {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int id;
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;

    public Cliente(String nome, String cpf, String email, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public Cliente(int id, String nome, String cpf, String email, LocalDate dataNascimento) {
        this(nome, cpf, email, dataNascimento);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    @Override
    public String toString() {
        return String.format("[%d] %s | CPF: %s | %s | nascimento: %s",
                id, nome, cpf, email, dataNascimento.format(FORMATO));
    }
}
