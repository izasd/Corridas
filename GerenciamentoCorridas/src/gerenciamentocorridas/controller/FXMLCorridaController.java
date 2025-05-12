package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.dao.AtletaDAO;
import gerenciamentocorridas.model.dao.CorridaDAO;
import gerenciamentocorridas.model.database.Database;
import gerenciamentocorridas.model.database.DatabaseFactory;
import gerenciamentocorridas.model.domain.Atleta;
import gerenciamentocorridas.model.domain.Corrida;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLCorridaController implements Initializable {
    @FXML
    private TableView<Corrida> tableCorridas;
    @FXML
    private TableColumn<Corrida, String> colEdicao;
    @FXML
    private TableColumn<Corrida, String> colPaisCorrida;
    @FXML
    private TableColumn<Corrida, String> colCategoria;
    @FXML
    private TableColumn<Corrida, String> colDistancia;
    @FXML
    private TableColumn<Corrida, String> colGeneroCorrida;
    @FXML
    private TableColumn<Corrida, String> colNAtletas;
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
    private TextField txtEdicao;
    @FXML
    private TextField txtPais;
    @FXML
    private ComboBox<String> comboBCategoria;
    @FXML
    private ComboBox<String> comboBDistancia;
    @FXML
    private ChoiceBox<String> choiceBGenero;
    @FXML
    private Spinner<Integer> spnQtdMin;
    @FXML
    private Spinner<Integer> spnQtdMax;
    @FXML
    private Button btnLimpar;
    @FXML
    private Button btnAdicionar;
    @FXML
    private Button btnRemover;
    @FXML
    private Button btnInserir;
    @FXML
    private Button btnAlterar;
    @FXML
    private Button btnExcluir;
    
    private List<Corrida> listCorridas;
    private ObservableList<Corrida> observableListCorridas;
    private List<Atleta> listAtletas;
    private ObservableList<Atleta> observableListAtletas;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CorridaDAO corridaDAO = new CorridaDAO();
    private final AtletaDAO atletaDAO = new AtletaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        corridaDAO.setConnection(connection);
        atletaDAO.setConnection(connection);
        spnQtdMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        spnQtdMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        choiceBGenero.getItems().addAll("Masculino", "Feminino", "Outro");
        carregarTableViewCorridas();
        carregarTableViewAtletas();
        selecionarItemTableViewCorridas(null);
        selecionarItemTableViewAtletas(null);
        tableCorridas.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selecionarItemTableViewCorridas(newValue));
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
        selecionarItemTableViewCorridas(null);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
