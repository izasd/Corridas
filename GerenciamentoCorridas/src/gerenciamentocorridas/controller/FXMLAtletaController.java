/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerenciamentocorridas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Henrique Machado
 */
public class FXMLAtletaController implements Initializable {

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtPais;
    @FXML
    private TableColumn<?, ?> colNome;
    @FXML
    private TableColumn<?, ?> colIdade;
    @FXML
    private TableColumn<?, ?> colGenero;
    @FXML
    private TableColumn<?, ?> colPais;
    @FXML
    private TableView<?> tableAtletas;
    @FXML
    private Button btnInserir;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnExcluir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }       
}
