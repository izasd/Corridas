package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.dao.AtletaDAO;
import gerenciamentocorridas.model.database.Database;
import gerenciamentocorridas.model.database.DatabaseFactory;
import gerenciamentocorridas.model.domain.Atleta;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLAtletaController implements Initializable {
    @FXML
    private TableView<Atleta> tableAtletas;
    @FXML
    private TableColumn<Atleta, String> colNome;
    @FXML
    private TableColumn<Atleta, String> colPais;
    @FXML
    private TableColumn<Atleta, String> colGenero;
    @FXML
    private TableColumn<Atleta, String> colIdade;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtPais;
    @FXML
    private ChoiceBox<String> choiceBGenero;
    @FXML
    private Spinner<Integer> spnIdade;
    @FXML
    private Spinner<Integer> spnBronze;
    @FXML
    private Spinner<Integer> spnPrata;
    @FXML
    private Spinner<Integer> spnOuro;
    @FXML
    private Button btnLimpar;
    @FXML
    private Button btnInserir;
    @FXML
    private Button btnAlterar;
    @FXML
    private Button btnExcluir;
    
    private List<Atleta> listAtletas;
    private ObservableList<Atleta> observableListAtletas;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final AtletaDAO atletaDAO = new AtletaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        atletaDAO.setConnection(connection);
        spnIdade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150));
        spnBronze.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        spnPrata.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        spnOuro.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        choiceBGenero.getItems().addAll("Masculino", "Feminino", "Outro");
        carregarTableViewAtletas();
        selecionarItemTableViewAtletas(null);
        tableAtletas.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selecionarItemTableViewAtletas(newValue));
    }

    public void carregarTableViewAtletas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));

        listAtletas = atletaDAO.listar();
        observableListAtletas = FXCollections.observableArrayList(listAtletas);
        tableAtletas.setItems(observableListAtletas);
    }

    public void selecionarItemTableViewAtletas(Atleta atleta) {
        if (atleta != null) {
            txtNome.setText(atleta.getNome());
            spnIdade.getValueFactory().setValue(atleta.getIdade());
            choiceBGenero.setValue(atleta.getGenero());
            txtPais.setText(atleta.getPais());
            spnBronze.getValueFactory().setValue(atleta.getBronze());
            spnPrata.getValueFactory().setValue(atleta.getPrata());
            spnOuro.getValueFactory().setValue(atleta.getOuro());
            btnLimpar.setDisable(false);
            btnInserir.setDisable(true);
            btnAlterar.setDisable(false);
            btnExcluir.setDisable(false);
        } else {
            txtNome.setText("");
            spnIdade.getValueFactory().setValue(0);
            choiceBGenero.setValue(null);
            txtPais.setText("");
            spnBronze.getValueFactory().setValue(0);
            spnPrata.getValueFactory().setValue(0);
            spnOuro.getValueFactory().setValue(0);
            btnLimpar.setDisable(true);
            btnInserir.setDisable(false);
            btnAlterar.setDisable(true);
            btnExcluir.setDisable(true);
        }
    }
    
    public void handleButtonLimpar() throws IOException {
        selecionarItemTableViewAtletas(null);
    }

    @FXML
    public void handleButtonInserir() throws IOException {
        if (txtNome.getText().isEmpty() || choiceBGenero.getValue() == null || txtPais.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Preencha todos os campos obrigatórios!");
            alert.show();
            return;
        }
        Atleta atleta = new Atleta();
        atleta.setNome(txtNome.getText());
        atleta.setIdade(spnIdade.getValue());
        atleta.setGenero(choiceBGenero.getValue());
        atleta.setPais(txtPais.getText());
        atleta.setBronze(spnBronze.getValue());
        atleta.setPrata(spnPrata.getValue());
        atleta.setOuro(spnOuro.getValue());
        if (atletaDAO.inserir(atleta)) {
            carregarTableViewAtletas();
            selecionarItemTableViewAtletas(null);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erro ao inserir atleta!");
            alert.show();
            }
    }

    @FXML
    public void handleButtonAlterar() throws IOException {
        Atleta atleta = tableAtletas.getSelectionModel().getSelectedItem();
        if (atleta != null) {
            atleta.setNome(txtNome.getText());
            atleta.setIdade(spnIdade.getValue());
            atleta.setGenero(choiceBGenero.getValue());
            atleta.setPais(txtPais.getText());
            atleta.setBronze(spnBronze.getValue());
            atleta.setPrata(spnPrata.getValue());
            atleta.setOuro(spnOuro.getValue());
            atletaDAO.alterar(atleta);
            carregarTableViewAtletas();
            selecionarItemTableViewAtletas(null);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um atleta na tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonExcluir() throws IOException {
        Atleta atleta = tableAtletas.getSelectionModel().getSelectedItem();
        if (atleta != null) {
            atletaDAO.remover(atleta);
            carregarTableViewAtletas();
            selecionarItemTableViewAtletas(null);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um atleta na tabela!");
            alert.show();
        }
    }
}
