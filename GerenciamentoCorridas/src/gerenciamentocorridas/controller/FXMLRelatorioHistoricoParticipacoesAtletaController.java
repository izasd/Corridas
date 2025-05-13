package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.database.Database;
import gerenciamentocorridas.model.database.DatabaseFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.*;
import java.util.*;

public class FXMLRelatorioHistoricoParticipacoesAtletaController implements Initializable {

    @FXML
    private TableView<Participacao> tableViewParticipacoes;
    @FXML
    private TableColumn<Participacao, String> colunaEdicao;
    @FXML
    private TableColumn<Participacao, String> colunaLocal;
    @FXML
    private TableColumn<Participacao, String> colunaCategoria;
    @FXML
    private TableColumn<Participacao, Double> colunaDistancia;
    @FXML
    private TableColumn<Participacao, String> colunaTempo;
    @FXML
    private TableColumn<Participacao, Integer> colunaColocacao;
    @FXML
    private Button buttonImprimir;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    private ObservableList<Participacao> listaParticipacoes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colunaEdicao.setCellValueFactory(new PropertyValueFactory<>("edicao"));
        colunaLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colunaDistancia.setCellValueFactory(new PropertyValueFactory<>("distancia"));
        colunaTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaColocacao.setCellValueFactory(new PropertyValueFactory<>("colocacao"));
    }

    @FXML
    public void handleImprimir() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Relatório de Participações");
        dialog.setHeaderText("Gerar histórico de participações do atleta");
        dialog.setContentText("Informe o nome do atleta:");

        dialog.showAndWait().ifPresent(nome -> {
            try {
                carregarParticipacoes(nome); // preenche o TableView

                Map<String, Object> parametros = new HashMap<>();
                parametros.put("atleta_nome", nome);

                URL url = getClass().getResource("/gerenciamentocorridas/relatorios/HistoricoParticipacoesAtleta.jasper");
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);

                JasperViewer viewer = new JasperViewer(jasperPrint, false);
                viewer.setTitle("Histórico de Participações do Atleta");
                viewer.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void carregarParticipacoes(String nomeAtleta) {
        listaParticipacoes.clear();

        String sql
                = "SELECT c.edicao, c.local, c.categoria, c.distancia, r.tempo, r.podio AS colocacao "
                + "FROM corrida c "
                + "JOIN corrida_atleta ca ON ca.corrida_id = c.id "
                + "JOIN atleta a ON a.id = ca.atleta_id "
                + "LEFT JOIN resultado_corrida r ON r.atleta_id = a.id AND r.corrida_id = c.id "
                + "WHERE a.nome ILIKE ? "
                + "ORDER BY c.edicao";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeAtleta + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tempo = rs.getString("tempo");
                Integer colocacao = rs.getObject("colocacao") != null ? rs.getInt("colocacao") : 0;

                Participacao p = new Participacao(
                        rs.getString("edicao"),
                        rs.getString("local"),
                        rs.getString("categoria"),
                        rs.getDouble("distancia"),
                        tempo,
                        colocacao
                );
                listaParticipacoes.add(p);
            }

            tableViewParticipacoes.setItems(listaParticipacoes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Classe interna simples para representar participações
    public static class Participacao {

        private String edicao;
        private String local;
        private String categoria;
        private double distancia;
        private String tempo;
        private int colocacao;

        public Participacao(String edicao, String local, String categoria, double distancia, String tempo, int colocacao) {
            this.edicao = edicao;
            this.local = local;
            this.categoria = categoria;
            this.distancia = distancia;
            this.tempo = tempo;
            this.colocacao = colocacao;
        }

        public String getEdicao() {
            return edicao;
        }

        public String getLocal() {
            return local;
        }

        public String getCategoria() {
            return categoria;
        }

        public double getDistancia() {
            return distancia;
        }

        public String getTempo() {
            return tempo;
        }

        public int getColocacao() {
            return colocacao;
        }
    }
}
