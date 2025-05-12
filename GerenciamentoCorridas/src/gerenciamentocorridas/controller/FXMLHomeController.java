package gerenciamentocorridas.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import gerenciamentocorridas.model.domain.Resultado;
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
    private MenuItem menuTabelas;
    @FXML
    private MenuItem menuGraficos;

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
        // placeholder
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
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/gerenciamentocorridas/view/FXMLResultado.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemTabelas() throws IOException {
        // Placeholder: Substituir quando as abas estiverem configuradas
        AnchorPane a = new AnchorPane();
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    public void handleMenuItemGraficos() throws IOException {
        // Placeholder: Substituir quando as abas estiverem configuradas
        AnchorPane a = new AnchorPane();
        anchorPane.getChildren().setAll(a);
    }
}