package sorveteria.dao;

import sorveteria.db.Conexao;
import sorveteria.model.Cliente;
import sorveteria.model.Pedido;
import sorveteria.model.Produto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    // Um único SELECT traz o pedido, o cliente e o produto (com a subclasse certa)
    private static final String SELECT_BASE =
            "SELECT ped.id AS ped_id, ped.quantidade AS ped_quantidade, ped.data_pedido AS ped_data_pedido, " +
            ClienteDAO.COLUNAS + ", " + ProdutoDAO.COLUNAS +
            "FROM pedido ped " +
            "JOIN cliente c ON c.id = ped.id_cliente " +
            "JOIN produto p ON p.id = ped.id_produto " +
            ProdutoDAO.JOINS;

    public void inserir(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (id_cliente, id_produto, quantidade, data_pedido) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pedido.getCliente().getId());
            ps.setInt(2, pedido.getProduto().getId());
            ps.setInt(3, pedido.getQuantidade());
            ps.setDate(4, Date.valueOf(pedido.getDataPedido()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    pedido.setId(keys.getInt(1));
                }
            }
        }
    }

    public List<Pedido> listar() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY ped.id";
        List<Pedido> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    public List<Pedido> buscarPorCpf(String cpf) throws SQLException {
        String sql = SELECT_BASE + "WHERE c.cpf = ? ORDER BY ped.id";
        List<Pedido> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    private Pedido montar(ResultSet rs) throws SQLException {
        Cliente cliente = ClienteDAO.montar(rs);
        Produto produto = ProdutoDAO.montar(rs);
        return new Pedido(
                rs.getInt("ped_id"),
                cliente,
                produto,
                rs.getInt("ped_quantidade"),
                rs.getDate("ped_data_pedido").toLocalDate());
    }
}
