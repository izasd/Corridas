package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.database.Database;
import gerenciamentocorridas.model.database.DatabaseFactory;
import gerenciamentocorridas.model.database.DatabasePostgreSQL;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import gerenciamentocorridas.model.domain.Resultado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FXMLHomeController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private MenuItem menuHome;
    @FXML
    private MenuItem menuAtletas;
    @FXML
    private MenuItem menuCorridas;
    @FXML
    private MenuItem menuResultados;
    @FXML
    private MenuItem menuRelAtletasPorPais;
    @FXML
    private MenuItem menuRelHistorico;
    @FXML
    private MenuItem menuRelParticipantesPorEdicao;
    @FXML
    private MenuItem menuRelMedalhasPorAtleta;
    @FXML
    private MenuItem menuSistemaGrupos;
    @FXML
    private MenuItem menuRelAbrir;
    @FXML
    private TableView<Resultado> tabelaResultados;
    @FXML
    private TableColumn<Resultado, Integer> colunaPosicao;
    @FXML
    private TableColumn<Resultado, String> colunaAtleta;
    @FXML
    private TableColumn<Resultado, String> colunaPais;
    @FXML
    private TableColumn<Resultado, Double> colunaTempo;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    @FXML
    public void initialize() {
        if (tabelaResultados != null) {
            colunaPosicao.setCellValueFactory(new PropertyValueFactory<>("posicao"));
            colunaAtleta.setCellValueFactory(new PropertyValueFactory<>("atleta"));
            colunaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
            colunaTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));

            //tabelaResultados.setItems(getResultadosOlimpiadas2024());
        }
    }

    //private ObservableList<Resultado> getResultadosOlimpiadas2024() {
    //}
    @FXML
    public void handleMenuItemHome() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/gerenciamentocorridas/view/FXMLHome.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemAtletas() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/gerenciamentocorridas/view/FXMLAtleta.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemCorridas() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/gerenciamentocorridas/view/FXMLCorrida.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemResultados() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gerenciamentocorridas/view/FXMLResultado.fxml"));
        AnchorPane a = loader.load();
        FXMLResultadoController controller = loader.getController();

        Database db = new DatabasePostgreSQL();
        controller.setConnection(db.conectar());
        controller.carregarCorridas();

        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemRelAtletasPorPais() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/gerenciamentocorridas/view/FXMLRelatorioAtletasPorPais.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemRelHistorico() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/gerenciamentocorridas/view/FXMLRelatorioHistoricoParticipacoesAtleta.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemRelParticipantesPorEdicao() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/gerenciamentocorridas/view/FXMLGraficoParticipantesEdicao.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemRelMedalhasPorAtleta() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/gerenciamentocorridas/view/FXMLGraficoMedalhasAtleta.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuRelAbrir() throws IOException {
        AnchorPane a = FXMLLoader.load(getClass().getResource("/sockets/thread/FXMLClienteSistemaGrupos.fxml"));
        anchorPane.getChildren().setAll(a);

    }

}
