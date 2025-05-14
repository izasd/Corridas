package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.database.Database;
import gerenciamentocorridas.model.database.DatabaseFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FXMLRelatorioHistoricoParticipacoesAtletaController implements Initializable {

    @FXML
    private TableView<Participacao> tableViewParticipacoes;
    @FXML
    private TableColumn<Participacao, Integer> colunaIdAtleta;
    @FXML
    private TableColumn<Participacao, String> colunaAtleta;
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

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    private ObservableList<Participacao> listaParticipacoes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colunaIdAtleta.setCellValueFactory(new PropertyValueFactory<>("idAtleta"));
        colunaAtleta.setCellValueFactory(new PropertyValueFactory<>("atleta"));
        colunaEdicao.setCellValueFactory(new PropertyValueFactory<>("edicao"));
        colunaLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colunaDistancia.setCellValueFactory(new PropertyValueFactory<>("distancia"));
        colunaTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        colunaColocacao.setCellValueFactory(new PropertyValueFactory<>("colocacao"));

        carregarTodasParticipacoes();
    }

    private void carregarTodasParticipacoes() {
        listaParticipacoes.clear();

        String sql
                = "SELECT a.id AS idAtleta, a.nome AS atleta, c.edicao, c.local, c.categoria, c.distancia, "
                + "r.tempo, r.podio AS colocacao "
                + "FROM corrida c "
                + "JOIN corrida_atleta ca ON ca.corrida_id = c.id "
                + "JOIN atleta a ON a.id = ca.atleta_id "
                + "LEFT JOIN resultado_corrida r ON r.atleta_id = a.id AND r.corrida_id = c.id "
                + "ORDER BY a.nome, c.edicao";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idAtleta = rs.getInt("idAtleta");
                String atleta = rs.getString("atleta");
                String edicao = rs.getString("edicao");
                String local = rs.getString("local");
                String categoria = rs.getString("categoria");
                double distancia = rs.getDouble("distancia");
                String tempo = rs.getString("tempo");
                int colocacao = rs.getObject("colocacao") != null ? rs.getInt("colocacao") : 0;

                listaParticipacoes.add(new Participacao(idAtleta, atleta, edicao, local, categoria, distancia, tempo, colocacao));
            }

            tableViewParticipacoes.setItems(listaParticipacoes);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleImprimir() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Filtro de Atleta");
        dialog.setHeaderText("Gerar relatório de histórico de participações");
        dialog.setContentText("Informe o ID do atleta:");

        dialog.showAndWait().ifPresent(idTexto -> {
            try {
                int atletaId = Integer.parseInt(idTexto);

                Map<String, Object> parametros = new HashMap<>();
                parametros.put("id_atleta", atletaId);

                URL url = getClass().getResource("/gerenciamentocorridas/relatorios/HistoricoParticipacoes.jasper");
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);

                JasperViewer viewer = new JasperViewer(jasperPrint, false);
                viewer.setTitle("Histórico de Participações do Atleta");
                viewer.setVisible(true);

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("ID inválido");
                alert.setContentText("Informe um número inteiro para o ID do atleta.");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao gerar o relatório");
                alert.setContentText("Detalhes: " + e.getMessage());
                alert.showAndWait();
            }
        });
    }

<<<<<<< HEAD
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
=======
>>>>>>> iza
    public static class Participacao {

        private final int idAtleta;
        private final String atleta;
        private final String edicao;
        private final String local;
        private final String categoria;
        private final double distancia;
        private final String tempo;
        private final int colocacao;

        public Participacao(int idAtleta, String atleta, String edicao, String local, String categoria, double distancia, String tempo, int colocacao) {
            this.idAtleta = idAtleta;
            this.atleta = atleta;
            this.edicao = edicao;
            this.local = local;
            this.categoria = categoria;
            this.distancia = distancia;
            this.tempo = tempo;
            this.colocacao = colocacao;
        }

        public int getIdAtleta() {
            return idAtleta;
        }

        public String getAtleta() {
            return atleta;
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
