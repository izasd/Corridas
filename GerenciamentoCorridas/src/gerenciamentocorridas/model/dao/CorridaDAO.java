package gerenciamentocorridas.model.dao;

import gerenciamentocorridas.model.domain.Corrida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorridaDAO {

    private final Connection connection;

    public CorridaDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Corrida corrida) throws SQLException {
        String sql = "INSERT INTO Corrida (edicao, local, categoria, genero, distancia, qtd_max_corr, qtd_min_corr) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, corrida.getEdicao());
            stmt.setString(2, corrida.getLocal());
            stmt.setString(3, corrida.getCategoria());
            stmt.setString(4, String.valueOf(corrida.getGenero()));
            stmt.setDouble(5, corrida.getDistancia());
            stmt.setInt(6, corrida.getQtdMaxCorr());
            stmt.setInt(7, corrida.getQtdMinCorr());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                corrida.setId(generatedKeys.getInt(1));
            }
        }
    }

    public void atualizar(Corrida corrida) throws SQLException {
        String sql = "UPDATE Corrida SET edicao = ?, local = ?, categoria = ?, genero = ?, distancia = ?, " +
                     "qtd_max_corr = ?, qtd_min_corr = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, corrida.getEdicao());
            stmt.setString(2, corrida.getLocal());
            stmt.setString(3, corrida.getCategoria());
            stmt.setString(4, String.valueOf(corrida.getGenero()));
            stmt.setDouble(5, corrida.getDistancia());
            stmt.setInt(6, corrida.getQtdMaxCorr());
            stmt.setInt(7, corrida.getQtdMinCorr());
            stmt.setInt(8, corrida.getId());

            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Corrida WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Corrida buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Corrida WHERE id = ?";
        Corrida corrida = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                corrida = new Corrida(
                    rs.getInt("id"),
                    rs.getString("edicao"),
                    rs.getString("local"),
                    rs.getString("categoria"),
                    rs.getString("genero").charAt(0),
                    rs.getDouble("distancia"),
                    rs.getInt("qtd_max_corr"),
                    rs.getInt("qtd_min_corr")
                );
            }
        }

        return corrida;
    }

    public List<Corrida> listarTodas() throws SQLException {
        List<Corrida> corridas = new ArrayList<>();
        String sql = "SELECT * FROM Corrida";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Corrida corrida = new Corrida(
                    rs.getInt("id"),
                    rs.getString("edicao"),
                    rs.getString("local"),
                    rs.getString("categoria"),
                    rs.getString("genero").charAt(0),
                    rs.getDouble("distancia"),
                    rs.getInt("qtd_max_corr"),
                    rs.getInt("qtd_min_corr")
                );
                corridas.add(corrida);
            }
        }

        return corridas;
    }
}
