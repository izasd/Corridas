/*package gerenciamentocorridas.model.dao;

import gerenciamentocorridas.model.domain.Medalha;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedalhaDAO {

    private final Connection connection;

    public MedalhaDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Medalha medalha) throws SQLException {
        String sql = "INSERT INTO Medalha (tipo) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, medalha.getTipo());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                medalha.setId(generatedKeys.getInt(1));
            }
        }
    }

    public void atualizar(Medalha medalha) throws SQLException {
        String sql = "UPDATE Medalha SET tipo = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, medalha.getTipo());
            stmt.setInt(2, medalha.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Medalha WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Medalha buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Medalha WHERE id = ?";
        Medalha medalha = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                medalha = new Medalha(
                    rs.getInt("id"),
                    rs.getString("tipo")
                );
            }
        }

        return medalha;
    }

    public List<Medalha> listarTodas() throws SQLException {
        List<Medalha> medalhas = new ArrayList<>();
        String sql = "SELECT * FROM Medalha";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medalha medalha = new Medalha(
                    rs.getInt("id"),
                    rs.getString("tipo")
                );
                medalhas.add(medalha);
            }
        }

        return medalhas;
    }
}*/
