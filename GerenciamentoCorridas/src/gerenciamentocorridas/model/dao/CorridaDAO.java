package gerenciamentocorridas.model.dao;

import gerenciamentocorridas.model.domain.Corrida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CorridaDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Corrida corrida) {
        String sql = "INSERT INTO Corrida(edicao, pais, categoria, distancia, genero, qtdAtletas, qtdMin, qtdMax) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, corrida.getEdicao());
            stmt.setString(2, corrida.getPais());
            stmt.setString(3, corrida.getCategoria());
            stmt.setDouble(4, corrida.getDistancia());
            stmt.setString(5, corrida.getGenero());
            stmt.setInt(6, corrida.getQtdAtletas());
            stmt.setInt(7, corrida.getQtdMin());
            stmt.setInt(8, corrida.getQtdMax());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CorridaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean atlterar(Corrida corrida) {
        String sql = "UPDATE Corrida SET edicao=?, pais=?, categoria=?, distancia=?, genero=?, qtdAtletas=?, qtdMin=?, qtdMax WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, corrida.getEdicao());
            stmt.setString(2, corrida.getPais());
            stmt.setString(3, corrida.getCategoria());
            stmt.setDouble(4, corrida.getDistancia());
            stmt.setString(5, corrida.getGenero());
            stmt.setInt(6, corrida.getQtdAtletas());
            stmt.setInt(7, corrida.getQtdMin());
            stmt.setInt(8, corrida.getQtdMax());
            stmt.setInt(9, corrida.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AtletaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Corrida corrida) {
        String sql = "DELETE FROM corrida WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, corrida.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CorridaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Corrida> listar() {
        String sql = "SELECT * FROM Corrida";
        List<Corrida> listaCorridas = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultado = stmt.executeQuery(sql);
            while (resultado.next()) {
                Corrida corrida = new Corrida();
                corrida.setId(resultado.getInt("id"));
                corrida.setEdicao(resultado.getString("edicao"));
                corrida.setPais(resultado.getString("pais"));
                corrida.setCategoria(resultado.getString("categoria"));
                corrida.setDistancia(resultado.getDouble("distancia"));
                corrida.setGenero(resultado.getString("genero"));
                corrida.setQtdAtletas(resultado.getInt("qtdAtletas"));
                corrida.setQtdMin(resultado.getInt("qtdMin"));
                corrida.setQtdMax(resultado.getInt("qtdMax"));
                listaCorridas.add(corrida);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CorridaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaCorridas;
    }
}
