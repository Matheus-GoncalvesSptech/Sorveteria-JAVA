package sorveteria.dao;

import sorveteria.db.Conexao;
import sorveteria.model.Picole;
import sorveteria.model.Produto;
import sorveteria.model.Sorvete;
import sorveteria.model.Tamanho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    // Colunas usadas por montar();
    static final String COLUNAS =
            "p.id, p.nome, p.preco_base, " +
            "s.id_produto AS sorvete_id, s.sabor, s.tamanho, " +
            "pc.id_produto AS picole_id, pc.tem_cobertura ";

    static final String JOINS =
            "LEFT JOIN sorvete s ON s.id_produto = p.id " +
            "LEFT JOIN picole pc ON pc.id_produto = p.id ";

    private static final String SELECT_BASE =
            "SELECT " + COLUNAS + "FROM produto p " + JOINS;

    // Insere um produto; grava também na tabela da subclasse (mesma transação)
    public void inserir(Produto produto) throws SQLException {
        String sqlProduto = "INSERT INTO produto (nome, preco_base) VALUES (?, ?)";
        String sqlSorvete = "INSERT INTO sorvete (id_produto, sabor, tamanho) VALUES (?, ?, ?)";
        String sqlPicole = "INSERT INTO picole (id_produto, tem_cobertura) VALUES (?, ?)";

        try (Connection conn = Conexao.getConexao()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(sqlProduto, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, produto.getNome());
                    ps.setDouble(2, produto.getPrecoBase());
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            produto.setId(keys.getInt(1));
                        }
                    }
                }

                if (produto instanceof Sorvete) {
                    Sorvete s = (Sorvete) produto;
                    try (PreparedStatement ps = conn.prepareStatement(sqlSorvete)) {
                        ps.setInt(1, s.getId());
                        ps.setString(2, s.getSabor());
                        ps.setString(3, s.getTamanho().name());
                        ps.executeUpdate();
                    }
                } else if (produto instanceof Picole) {
                    Picole pc = (Picole) produto;
                    try (PreparedStatement ps = conn.prepareStatement(sqlPicole)) {
                        ps.setInt(1, pc.getId());
                        ps.setBoolean(2, pc.isTemCobertura());
                        ps.executeUpdate();
                    }
                } else {
                    throw new IllegalArgumentException("Tipo de produto não suportado: " + produto.getClass().getSimpleName());
                }

                conn.commit();
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Produto> listar() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY p.id";
        List<Produto> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
        }
        return lista;
    }

    public List<Produto> buscarPorNome(String termo) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.nome LIKE ? ORDER BY p.id";
        List<Produto> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    // Devolve o produto ou null se o id não existir
    public Produto buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        }
        return null;
    }

    // Decide qual subclasse criar conforme a tabela que tem linha para o produto
    static Produto montar(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        double precoBase = rs.getDouble("preco_base");

        if (rs.getObject("sorvete_id") != null) {
            return new Sorvete(id, nome, precoBase,
                    rs.getString("sabor"), Tamanho.valueOf(rs.getString("tamanho")));
        }
        if (rs.getObject("picole_id") != null) {
            return new Picole(id, nome, precoBase, rs.getBoolean("tem_cobertura"));
        }
        throw new SQLException("O produto " + id + " não é nem sorvete nem picolé.");
    }
}
