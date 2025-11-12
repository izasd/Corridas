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
    private TextField txtQtdMin;
    @FXML
    private TextField txtQtdMax;
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
            txtQtdMin.setText(String.valueOf(corrida.getQtdMinCorr()));
            txtQtdMax.setText(String.valueOf(corrida.getQtdMaxCorr()));
            listaAtletasCorrida.setAll(corrida.getAtletas());

        } else {
            txtEdicao.clear();
            txtPais.clear();
            comboBCategoria.setValue(null);
            comboBDistancia.setValue(null);
            choiceBGenero.setValue(null);
            txtQtdMin.clear();
            txtQtdMax.clear();
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
        Integer qtdMin = validarInteiro(txtQtdMin, "Quantidade Mínima");
        Integer qtdMax = validarInteiro(txtQtdMax, "Quantidade Máxima");
        if (qtdMin == null || qtdMax == null) {
            return;
        }
        corrida.setQtdMinCorr(qtdMin);
        corrida.setQtdMaxCorr(qtdMax);
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
            Integer qtdMin = validarInteiro(txtQtdMin, "Quantidade Mínima");
            Integer qtdMax = validarInteiro(txtQtdMax, "Quantidade Máxima");
            if (qtdMin == null || qtdMax == null) {
                return;
            }
            corrida.setQtdMinCorr(qtdMin);
            corrida.setQtdMaxCorr(qtdMax);
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
        Corrida corridaSelecionada = tableCorridas.getSelectionModel().getSelectedItem();

        if (selecionado == null || corridaSelecionada == null) {
            mostrarAlerta("Erro", "Selecione um atleta e uma corrida.");
            return;
        }

        // 1. Já está inscrito?
        if (listaAtletasCorrida.contains(selecionado)
                || corridaDAO.atletaJaInscrito(selecionado.getId(), corridaSelecionada.getId())) {
            mostrarAlerta("Regras de Inscrição", "Este atleta já está inscrito nesta corrida.");
            return;
        }

        // 2. Máximo 3 atletas do mesmo país
        long mesmosPais = listaAtletasCorrida.stream()
                .filter(a -> a.getPais().equalsIgnoreCase(selecionado.getPais()))
                .count();
        if (mesmosPais >= 3) {
            mostrarAlerta("Regras de Inscrição", "Já existem 3 atletas deste país inscritos.");
            return;
        }

        // 3. Gênero correspondente
        String generoCorrida = choiceBGenero.getValue();
        if (!selecionado.getGenero().equalsIgnoreCase(generoCorrida)) {
            mostrarAlerta("Regras de Inscrição", "O gênero do atleta não corresponde ao da corrida.");
            return;
        }

        // 4. Limite de corredores
        Integer limite = validarInteiro(txtQtdMax, "Quantidade Máxima");
        if (limite == null) {
            return;
        }
        
        if (listaAtletasCorrida.size() >= limite) {
            mostrarAlerta("Regras de Inscrição", "Limite de atletas para esta corrida atingido.");
            return;
        }

        listaAtletasCorrida.add(selecionado);
    }

    @FXML
    public void handleButtonRemoverAtleta(ActionEvent event) {
        ObservableList<Atleta> selecionados = tableViewAtletas.getSelectionModel().getSelectedItems();
        if (!selecionados.isEmpty()) {
            listaAtletasCorrida.removeAll(selecionados);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private Integer validarInteiro(TextField campo, String nomeCampo) {
        try {
            return Integer.parseInt(campo.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de validação");
            alert.setHeaderText(null);
            alert.setContentText("O campo \"" + nomeCampo + "\" deve conter um número inteiro válido.");
            alert.showAndWait();
            return null;
        }
    }

}
