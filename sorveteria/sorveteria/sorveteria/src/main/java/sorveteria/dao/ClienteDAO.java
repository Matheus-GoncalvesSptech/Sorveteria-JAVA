package sorveteria.dao;

import sorveteria.db.Conexao;
import sorveteria.model.Cliente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Colunas com prefixo cli_ para não conflitar com as de produto/pedido nos JOINs
    static final String COLUNAS =
            "c.id AS cli_id, c.nome AS cli_nome, c.cpf AS cli_cpf, " +
            "c.email AS cli_email, c.data_nascimento AS cli_data_nascimento ";

    private static final String SELECT_BASE = "SELECT " + COLUNAS + "FROM cliente c ";

    public void inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, cpf, email, data_nascimento) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getEmail());
            ps.setDate(4, Date.valueOf(cliente.getDataNascimento()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    cliente.setId(keys.getInt(1));
                }
            }
        }
    }

    public List<Cliente> listar() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY c.id";
        List<Cliente> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    // Devolve o cliente ou null se o CPF não estiver cadastrado
    public Cliente buscarPorCpf(String cpf) throws SQLException {
        String sql = SELECT_BASE + "WHERE c.cpf = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        }
        return null;
    }

    static Cliente montar(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("cli_id"),
                rs.getString("cli_nome"),
                rs.getString("cli_cpf"),
                rs.getString("cli_email"),
                rs.getDate("cli_data_nascimento").toLocalDate());
    }
}
