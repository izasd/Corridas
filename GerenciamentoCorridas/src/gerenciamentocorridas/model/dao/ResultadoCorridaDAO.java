package gerenciamentocorridas.model.dao;

import gerenciamentocorridas.model.domain.Resultado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultadoCorridaDAO {

    private final Connection connection;

    public ResultadoCorridaDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Resultado resultado) throws SQLException {
        String sql = "INSERT INTO Resultado_Corrida (atleta_id, corrida_id, podio, tempo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, resultado.getAtletaId());
            stmt.setInt(2, resultado.getCorridaId());

            if (resultado.getPodio() != null) {
                stmt.setInt(3, resultado.getPodio());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, resultado.getTempo()); // assume que tempo está em formato PostgreSQL válido: '00:10:35'

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                resultado.setId(generatedKeys.getInt(1));
            }
        }
    }

    public void atualizar(Resultado resultado) throws SQLException {
        String sql = "UPDATE Resultado_Corrida SET atleta_id = ?, corrida_id = ?, podio = ?, tempo = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, resultado.getAtletaId());
            stmt.setInt(2, resultado.getCorridaId());

            if (resultado.getPodio() != null) {
                stmt.setInt(3, resultado.getPodio());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, resultado.getTempo());
            stmt.setInt(5, resultado.getId());

            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Resultado_Corrida WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Resultado buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Resultado_Corrida WHERE id = ?";
        Resultado resultado = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                resultado = new Resultado(
                    rs.getInt("id"),
                    rs.getInt("atleta_id"),
                    rs.getInt("corrida_id"),
                    rs.getObject("podio") != null ? rs.getInt("podio") : null,
                    rs.getString("tempo") // INTERVAL convertido para string
                );
            }
        }

        return resultado;
    }

    public List<Resultado> listarTodos() throws SQLException {
        List<Resultado> resultados = new ArrayList<>();
        String sql = "SELECT * FROM Resultado_Corrida";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Resultado resultado = new Resultado(
                    rs.getInt("id"),
                    rs.getInt("atleta_id"),
                    rs.getInt("corrida_id"),
                    rs.getObject("podio") != null ? rs.getInt("podio") : null,
                    rs.getString("tempo")
                );
                resultados.add(resultado);
            }
        }

        return resultados;
    }
}
