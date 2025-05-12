package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.dao.AtletaDAO;
import gerenciamentocorridas.model.dao.CorridaDAO;
import gerenciamentocorridas.model.database.Database;
import gerenciamentocorridas.model.database.DatabaseFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import gerenciamentocorridas.model.domain.Atleta;
import gerenciamentocorridas.model.domain.Corrida;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class FXMLCorridaController {

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
    private TableView<Corrida> tableCorridas;
    @FXML
    private TableColumn<Corrida, String> columnEdicao;
    @FXML
    private TableColumn<Corrida, String> columnPais;
    @FXML
    private TableView<Atleta> tableAtletas;
    @FXML
    private TableColumn<Atleta, String> columnNome;
    @FXML
    private TableColumn<Atleta, Integer> columnIdade;
    @FXML
    private ComboBox<Atleta> comboBoxAtletas;
    @FXML
    private TableView<Atleta> tableViewAtletas;
    @FXML
    private Button btnAdicionarAtleta;
    @FXML
    private Button btnRemoverAtleta;

    private List<Corrida> listCorridas;
    private ObservableList<Corrida> observableListCorridas;
    private List<Atleta> listAtletas;
    private ObservableList<Atleta> observableListAtletas;
    private final CorridaDAO corridaDAO = new CorridaDAO();
    private final AtletaDAO atletaDAO = new AtletaDAO();
    private ObservableList<Atleta> listaAtletasDisponiveis = FXCollections.observableArrayList();
    private ObservableList<Atleta> listaAtletasCorrida = FXCollections.observableArrayList();
    
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    @FXML
    public void initialize() {
        carregarTableViewCorridas();
        carregarTableViewAtletas();
        carregarAtletasDisponiveis();
        configurarTableView();

        tableAtletas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableCorridas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCorridas(newValue));
    }

    public void carregarTableViewCorridas() {
        columnEdicao.setCellValueFactory(new PropertyValueFactory<>("edicao"));
        columnPais.setCellValueFactory(new PropertyValueFactory<>("pais"));

        listCorridas = corridaDAO.listar();
        observableListCorridas = FXCollections.observableArrayList(listCorridas);
        tableCorridas.setItems(observableListCorridas);
    }

    public void carregarTableViewAtletas() {
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));

        listAtletas = atletaDAO.listar();
        observableListAtletas = FXCollections.observableArrayList(listAtletas);
        tableAtletas.setItems(observableListAtletas);
    }

    private void carregarAtletasDisponiveis() {
        atletaDAO.setConnection(connection);
        List<Atleta> atletas = atletaDAO.listar();
        listaAtletasDisponiveis.setAll(atletas);
        comboBoxAtletas.setItems(listaAtletasDisponiveis);
    }

    private void configurarTableView() {
        tableViewAtletas.setItems(listaAtletasCorrida);
    }

    public void selecionarItemTableViewCorridas(Corrida corrida) {
        if (corrida != null) {
            txtEdicao.setText(corrida.getEdicao());
            txtPais.setText(corrida.getPais());
            comboBCategoria.setValue(corrida.getCategoria());
            comboBDistancia.setValue(String.valueOf(corrida.getDistancia()));
            choiceBGenero.setValue(corrida.getGenero());
            spnQtdMin.getValueFactory().setValue(corrida.getQtdMinCorr());
            spnQtdMax.getValueFactory().setValue(corrida.getQtdMaxCorr());

        } else {
            txtEdicao.clear();
            txtPais.clear();
            comboBCategoria.setValue(null);
            comboBDistancia.setValue(null);
            choiceBGenero.setValue(null);
            spnQtdMin.getValueFactory().setValue(0);
            spnQtdMax.getValueFactory().setValue(0);
        }
    }

    @FXML
    public void handleButtonLimpar() {
        selecionarItemTableViewCorridas(null);
    }

    @FXML
    public void handleButtonInserir() throws IOException {
        if (txtEdicao.getText().isEmpty() || txtPais.getText().isEmpty()
                || comboBCategoria.getValue() == null || comboBDistancia.getValue() == null
                || choiceBGenero.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Preencha todos os campos obrigatórios!");
            alert.show();
            return;
        }

        Corrida corrida = new Corrida();
        corrida.setEdicao(txtEdicao.getText());
        corrida.setPais(txtPais.getText());
        corrida.setCategoria(comboBCategoria.getValue());
        corrida.setDistancia(Double.parseDouble(comboBDistancia.getValue()));
        corrida.setGenero(choiceBGenero.getValue());
        corrida.setQtdMinCorr(spnQtdMin.getValue());
        corrida.setQtdMaxCorr(spnQtdMax.getValue());

        List<Atleta> atletasSelecionados = tableAtletas.getSelectionModel().getSelectedItems();
        corrida.setAtletas(atletasSelecionados);

        if (corridaDAO.inserir(corrida)) {
            carregarTableViewCorridas();
            selecionarItemTableViewCorridas(null);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erro ao inserir corrida!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonAlterar() throws IOException {
        Corrida corrida = tableCorridas.getSelectionModel().getSelectedItem();
        if (corrida != null) {
            corrida.setEdicao(txtEdicao.getText());
            corrida.setPais(txtPais.getText());
            corrida.setCategoria(comboBCategoria.getValue());
            corrida.setDistancia(Double.parseDouble(comboBDistancia.getValue()));
            corrida.setGenero(choiceBGenero.getValue());
            corrida.setQtdMinCorr(spnQtdMin.getValue());
            corrida.setQtdMaxCorr(spnQtdMax.getValue());
            corrida.setAtletas(tableAtletas.getSelectionModel().getSelectedItems());

            corridaDAO.alterar(corrida);
            carregarTableViewCorridas();
        }
    }

    @FXML
    public void handleButtonExcluir() throws IOException {
        Corrida corrida = tableCorridas.getSelectionModel().getSelectedItem();
        if (corrida != null) {
            corridaDAO.remover(corrida);
            carregarTableViewCorridas();
        }
    }

    @FXML
    public void handleButtonAdicionarAtleta(ActionEvent event) {
        Atleta selecionado = comboBoxAtletas.getValue();
        if (selecionado != null && !listaAtletasCorrida.contains(selecionado)) {
            listaAtletasCorrida.add(selecionado);
        }
    }

    @FXML
    public void handleButtonRemoverAtleta(ActionEvent event) {
        Atleta selecionado = tableViewAtletas.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            listaAtletasCorrida.remove(selecionado);
        }
    }

}
