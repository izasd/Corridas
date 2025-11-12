package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.database.Database;
import gerenciamentocorridas.model.database.DatabaseFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

public class FXMLGraficoMedalhasAtletaController implements Initializable {

    @FXML
    private PieChart graficoPizza;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Selecionar Atleta");
        dialog.setHeaderText("Gráfico de Medalhas");
        dialog.setContentText("Informe o ID do atleta:");

        dialog.showAndWait().ifPresent(idTexto -> {
            try {
                int atletaId = Integer.parseInt(idTexto);
                carregarMedalhas(atletaId);
            } catch (NumberFormatException e) {
                mostrarErro("ID inválido. Digite um número inteiro.");
            }
        });
    }

    private void carregarMedalhas(int atletaId) {
        try {
            String sql = "SELECT ouro, prata, bronze FROM atleta WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, atletaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int ouro = rs.getInt("ouro");
                int prata = rs.getInt("prata");
                int bronze = rs.getInt("bronze");

                ObservableList<PieChart.Data> dados = FXCollections.observableArrayList(
                        new PieChart.Data("Ouro", ouro),
                        new PieChart.Data("Prata", prata),
                        new PieChart.Data("Bronze", bronze)
                );

                graficoPizza.setData(dados);
                graficoPizza.setTitle("Medalhas do Atleta");
            } else {
                mostrarErro("Atleta com ID " + atletaId + " não encontrado.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarErro("Erro ao consultar o banco de dados.");
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}
