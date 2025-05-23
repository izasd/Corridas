package gerenciamentocorridas.model.dao;

import gerenciamentocorridas.model.domain.Atleta;
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
        String sql = "INSERT INTO Corrida(edicao, local, categoria, distancia, genero, qtd_atletas, qtd_min_corr, qtd_max_corr) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, corrida.getEdicao());
            stmt.setString(2, corrida.getLocal());
            stmt.setString(3, corrida.getCategoria());
            stmt.setDouble(4, corrida.getDistancia());
            stmt.setString(5, corrida.getGenero());
            stmt.setInt(6, 0);
            stmt.setInt(7, corrida.getQtdMinCorr());
            stmt.setInt(8, corrida.getQtdMaxCorr());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            int idCorrida = -1;
            if (generatedKeys.next()) {
                idCorrida = generatedKeys.getInt(1);
                corrida.setId(idCorrida);
            }

            corrida.setId(idCorrida);

            if (corrida.getAtletas() != null) {
                for (Atleta atleta : corrida.getAtletas()) {
                    String sqlAssoc = "INSERT INTO Corrida_Atleta(corrida_id, atleta_id) VALUES (?, ?)";
                    PreparedStatement assocStmt = connection.prepareStatement(sqlAssoc);
                    assocStmt.setInt(1, corrida.getId());
                    assocStmt.setInt(2, atleta.getId());
                    assocStmt.executeUpdate();

                    incrementarQtdAtletas(corrida.getId());
                }
            }

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
                corrida.setLocal(resultado.getString("local")); // novamente, campo é "local"
                corrida.setCategoria(resultado.getString("categoria"));
                corrida.setDistancia(resultado.getDouble("distancia"));
                corrida.setGenero(resultado.getString("genero"));
                corrida.setQtdMinCorr(resultado.getInt("qtd_min_corr"));
                corrida.setQtdMaxCorr(resultado.getInt("qtd_max_corr"));

                String sqlAtletas = "SELECT a.* FROM Atleta a "
                        + "INNER JOIN Corrida_Atleta ca ON a.id = ca.atleta_id "
                        + "WHERE ca.corrida_id = ?";

                PreparedStatement stmtAtleta = connection.prepareStatement(sqlAtletas);
                stmtAtleta.setInt(1, corrida.getId());
                ResultSet rsAtletas = stmtAtleta.executeQuery();
                List<Atleta> atletas = new ArrayList<>();
                while (rsAtletas.next()) {
                    Atleta atleta = new Atleta();
                    atleta.setId(rsAtletas.getInt("id"));
                    atleta.setNome(rsAtletas.getString("nome"));
                    atleta.setIdade(rsAtletas.getInt("idade"));
                    atleta.setGenero(rsAtletas.getString("genero"));
                    atleta.setPais(rsAtletas.getString("pais"));
                    atleta.setBronze(rsAtletas.getInt("bronze"));
                    atleta.setPrata(rsAtletas.getInt("prata"));
                    atleta.setOuro(rsAtletas.getInt("ouro"));
                    atletas.add(atleta);
                }
                corrida.setAtletas(atletas);
                corrida.setQtdAtletas(atletas.size());

                listaCorridas.add(corrida);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CorridaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaCorridas;
    }

    public boolean alterar(Corrida corrida) {
        String sql = "UPDATE Corrida SET edicao=?, local=?, categoria=?, distancia=?, genero=?, qtd_min_corr=?, qtd_max_corr=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, corrida.getEdicao());
            stmt.setString(2, corrida.getLocal());
            stmt.setString(3, corrida.getCategoria());
            stmt.setDouble(4, corrida.getDistancia());
            stmt.setString(5, corrida.getGenero());
            stmt.setInt(6, corrida.getQtdMinCorr());
            stmt.setInt(7, corrida.getQtdMaxCorr());
            stmt.setInt(8, corrida.getId());
            stmt.executeUpdate();

            String sqlDeleteVinculos = "DELETE FROM Corrida_Atleta WHERE corrida_id = ?";
            PreparedStatement stmtDelete = connection.prepareStatement(sqlDeleteVinculos);
            stmtDelete.setInt(1, corrida.getId());
            stmtDelete.executeUpdate();

            atualizarQtdAtletas(corrida.getId(), 0);

            if (corrida.getAtletas() != null) {
                for (Atleta atleta : corrida.getAtletas()) {
                    String sqlAssoc = "INSERT INTO Corrida_Atleta(corrida_id, atleta_id) VALUES (?, ?)";
                    PreparedStatement stmtAssoc = connection.prepareStatement(sqlAssoc);
                    stmtAssoc.setInt(1, corrida.getId());
                    stmtAssoc.setInt(2, atleta.getId());
                    stmtAssoc.executeUpdate();

                    incrementarQtdAtletas(corrida.getId());
                }
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CorridaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Corrida corrida) {
        String sql = "DELETE FROM corrida WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, corrida.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CorridaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void incrementarQtdAtletas(int corridaId) throws SQLException {
        String sql = "UPDATE Corrida SET qtd_atletas = qtd_atletas + 1 WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, corridaId);
        stmt.executeUpdate();
    }

    public void decrementarQtdAtletas(int corridaId) throws SQLException {
        String sql = "UPDATE Corrida SET qtd_atletas = qtd_atletas - 1 WHERE id = ? AND qtd_atletas > 0";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, corridaId);
        stmt.executeUpdate();
    }

    public void atualizarQtdAtletas(int corridaId, int novaQuantidade) throws SQLException {
        String sql = "UPDATE Corrida SET qtd_atletas = ? WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, novaQuantidade);
        stmt.setInt(2, corridaId);
        stmt.executeUpdate();
    }

}
