package gerenciamentocorridas.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OpcaoDAO {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public List<String> listarCategorias() {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT nome FROM categoria ORDER BY nome";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categorias.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public List<String> listarDistancias() {
        List<String> distancias = new ArrayList<>();
        String sql = "SELECT valor FROM distancia ORDER BY valor";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                distancias.add(String.valueOf(rs.getDouble("valor")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distancias;
    }
}
