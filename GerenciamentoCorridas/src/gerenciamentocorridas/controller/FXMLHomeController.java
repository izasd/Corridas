package gerenciamentocorridas.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class FXMLHomeController {

    @FXML
    private AnchorPane anchorPane;
    
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
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/gerenciamentocorridas/view/FXMLTabela.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemGraficos() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/gerenciamentocorridas/view/FXMLGrafico.fxml"));
        anchorPane.getChildren().setAll(a);
    }
}
