package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.dao.AtletaDAO;
import gerenciamentocorridas.model.dao.CorridaDAO;
import gerenciamentocorridas.model.dao.OpcaoDAO;
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
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
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
    private TableColumn<Corrida, String> columnPaisCorrida;
    @FXML
    private TableColumn<Corrida, String> columnCategoria;
    @FXML
    private TableColumn<Corrida, String> columnDistancia;
    @FXML
    private TableColumn<Corrida, String> columnGeneroCorrida;
    @FXML
    private TableColumn<Corrida, String> columnNAtletas;
    @FXML
    private TableColumn<Atleta, String> columnNome;
    @FXML
    private TableColumn<Atleta, Integer> columnIdade;
    @FXML
    private TableColumn<Atleta, String> columnPais;
    @FXML
    private TableColumn<Atleta, String> columnGenero;
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
    private final OpcaoDAO opcaoDAO = new OpcaoDAO();

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    @FXML
    public void initialize() {
        corridaDAO.setConnection(connection);
        atletaDAO.setConnection(connection);
        opcaoDAO.setConnection(connection);
        carregarComboBoxCategoriaDistanciaGenero();
        carregarTableViewCorridas();
        carregarTableViewAtletas();
        carregarAtletasDisponiveis();
        configurarTableView();

        tableViewAtletas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableCorridas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCorridas(newValue));

        spnQtdMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        spnQtdMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));

    }

    public void carregarTableViewCorridas() {
        columnEdicao.setCellValueFactory(new PropertyValueFactory<>("edicao"));
        columnPaisCorrida.setCellValueFactory(new PropertyValueFactory<>("local"));
        columnCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        columnDistancia.setCellValueFactory(new PropertyValueFactory<>("distancia"));
        columnGeneroCorrida.setCellValueFactory(new PropertyValueFactory<>("genero"));
        columnNAtletas.setCellValueFactory(new PropertyValueFactory<>("qtdAtletas"));

        listCorridas = corridaDAO.listar();
        observableListCorridas = FXCollections.observableArrayList(listCorridas);
        tableCorridas.setItems(observableListCorridas);
    }

    public void carregarTableViewAtletas() {
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        columnPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        columnGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        listAtletas = atletaDAO.listar();
        observableListAtletas = FXCollections.observableArrayList(listAtletas);
        tableViewAtletas.setItems(observableListAtletas);
    }

    private void carregarAtletasDisponiveis() {
        atletaDAO.setConnection(connection);
        List<Atleta> atletas = atletaDAO.listar();
        listaAtletasDisponiveis.setAll(atletas);
        comboBoxAtletas.setItems(listaAtletasDisponiveis);
    }

    private void carregarComboBoxCategoriaDistanciaGenero() {
        List<String> categorias = opcaoDAO.listarCategorias();
        comboBCategoria.setItems(FXCollections.observableArrayList(categorias));

        List<String> distancias = opcaoDAO.listarDistancias();
        comboBDistancia.setItems(FXCollections.observableArrayList(distancias));

        choiceBGenero.setItems(FXCollections.observableArrayList("M", "F"));
    }

    private void configurarTableView() {
        tableViewAtletas.setItems(listaAtletasCorrida);
    }

    public void selecionarItemTableViewCorridas(Corrida corrida) {
        if (corrida != null) {
            txtEdicao.setText(corrida.getEdicao());
            txtPais.setText(corrida.getLocal());
            comboBCategoria.setValue(corrida.getCategoria());
            comboBDistancia.setValue(String.valueOf(corrida.getDistancia()));
            choiceBGenero.setValue(corrida.getGenero());
            spnQtdMin.getValueFactory().setValue(corrida.getQtdMinCorr());
            spnQtdMax.getValueFactory().setValue(corrida.getQtdMaxCorr());
            listaAtletasCorrida.setAll(corrida.getAtletas());

        } else {
            txtEdicao.clear();
            txtPais.clear();
            comboBCategoria.setValue(null);
            comboBDistancia.setValue(null);
            choiceBGenero.setValue(null);
            spnQtdMin.getValueFactory().setValue(0);
            spnQtdMax.getValueFactory().setValue(0);

            listaAtletasCorrida.clear();
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
        corrida.setLocal(txtPais.getText());
        corrida.setCategoria(comboBCategoria.getValue());
        corrida.setDistancia(Double.parseDouble(comboBDistancia.getValue()));
        corrida.setGenero(choiceBGenero.getValue());
        corrida.setQtdMinCorr(spnQtdMin.getValue());
        corrida.setQtdMaxCorr(spnQtdMax.getValue());
        corrida.setAtletas(new ArrayList<>(listaAtletasCorrida));

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
            corrida.setLocal(txtPais.getText());
            corrida.setCategoria(comboBCategoria.getValue());
            corrida.setDistancia(Double.parseDouble(comboBDistancia.getValue()));
            corrida.setGenero(choiceBGenero.getValue());
            corrida.setQtdMinCorr(spnQtdMin.getValue());
            corrida.setQtdMaxCorr(spnQtdMax.getValue());
            corrida.setAtletas(tableViewAtletas.getSelectionModel().getSelectedItems());

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
        ObservableList<Atleta> selecionados = tableViewAtletas.getSelectionModel().getSelectedItems();
        if (!selecionados.isEmpty()) {
            listaAtletasCorrida.removeAll(selecionados);
        }
    }

}
