package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.database.Database;
import gerenciamentocorridas.model.database.DatabaseFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class FXMLGraficoParticipantesEdicaoController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis eixoEdicao;

    @FXML
    private NumberAxis eixoParticipantes;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarDados();
    }

    private void carregarDados() {
        String sql = "SELECT edicao, COUNT(ca.atleta_id) AS participantes " +
                     "FROM corrida c " +
                     "LEFT JOIN corrida_atleta ca ON ca.corrida_id = c.id " +
                     "GROUP BY edicao " +
                     "ORDER BY edicao";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName("Participantes por Edição");

            while (rs.next()) {
                String edicao = rs.getString("edicao");
                int participantes = rs.getInt("participantes");
                serie.getData().add(new XYChart.Data<>(edicao, participantes));
            }

            barChart.getData().add(serie);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
