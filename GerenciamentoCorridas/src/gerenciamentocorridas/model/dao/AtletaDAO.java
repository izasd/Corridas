package gerenciamentocorridas.model.dao;

import gerenciamentocorridas.model.domain.Atleta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AtletaDAO {

    private Connection connection;
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Atleta atleta) {
        String sql = "INSERT INTO atleta(nome, idade, genero, pais, bronze, prata, ouro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, atleta.getNome());
            stmt.setInt(2, atleta.getIdade());
            stmt.setString(3, atleta.getGenero());
            stmt.setString(4, atleta.getPais());
            stmt.setInt(5, atleta.getBronze());
            stmt.setInt(6, atleta.getPrata());
            stmt.setInt(7, atleta.getOuro());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AtletaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Atleta atleta) {
        String sql = "UPDATE atleta SET nome=?, idade=?, genero=?, pais=?, bronze=?, prata=?, ouro=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, atleta.getNome());
            stmt.setInt(2, atleta.getIdade());
            stmt.setString(3, atleta.getGenero());
            stmt.setString(4, atleta.getPais());
            stmt.setInt(5, atleta.getBronze());
            stmt.setInt(6, atleta.getPrata());
            stmt.setInt(7, atleta.getOuro());
            stmt.setInt(8, atleta.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AtletaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Atleta atleta) {
        String sql = "DELETE FROM atleta WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, atleta.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AtletaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Atleta> listar() {
        String sql = "SELECT * FROM atleta";
        List<Atleta> listaAtletas = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultado = stmt.executeQuery(sql);
            while (resultado.next()) {
                Atleta atleta = new Atleta();
                atleta.setId(resultado.getInt("id"));
                atleta.setNome(resultado.getString("nome"));
                atleta.setIdade(resultado.getInt("idade"));
                atleta.setGenero(resultado.getString("genero"));
                atleta.setPais(resultado.getString("pais"));
                atleta.setBronze(resultado.getInt("bronze"));
                atleta.setPrata(resultado.getInt("prata"));
                atleta.setOuro(resultado.getInt("ouro"));
                listaAtletas.add(atleta);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AtletaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaAtletas;
    }
}
