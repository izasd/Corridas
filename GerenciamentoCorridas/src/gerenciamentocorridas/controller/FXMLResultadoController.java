package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.dao.CorridaDAO;
import gerenciamentocorridas.model.dao.ResultadoCorridaDAO;
import gerenciamentocorridas.model.domain.Atleta;
import gerenciamentocorridas.model.domain.Corrida;
import gerenciamentocorridas.model.domain.Resultado;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

public class FXMLResultadoController {

    @FXML
    private TableView<Corrida> tabelaCorridas;
    @FXML
    private TableColumn<Corrida, String> colunaEdicao;
    @FXML
    private TableColumn<Corrida, String> colunaLocal;
    @FXML
    private TableColumn<Corrida, String> colunaCategoria;
    @FXML
    private TableColumn<Corrida, String> colunaGenero;
    @FXML
    private TableColumn<Corrida, Double> colunaDistancia;
    @FXML
    private TableView<Resultado> tabelaResultados;
    @FXML
    private TableColumn<Resultado, Integer> colunaPodio;
    @FXML
    private TableColumn<Resultado, String> colunaAtleta;
    @FXML
    private TableColumn<Resultado, String> colunaTempo;
    @FXML
    private Button btnInserir;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnExcluir;

    private Connection connection;
    private CorridaDAO corridaDAO;
    private ResultadoCorridaDAO resultadoDAO;
    private final Map<Integer, String> cacheNomesAtletas = new HashMap<>();

    private ObservableList<Corrida> corridasList = FXCollections.observableArrayList();
    private ObservableList<Resultado> resultadosList = FXCollections.observableArrayList();

    public void setConnection(Connection connection) {
        this.connection = connection;
        this.corridaDAO = new CorridaDAO();
        this.resultadoDAO = new ResultadoCorridaDAO(connection);
        corridaDAO.setConnection(connection);
    }

    @FXML
    public void initialize() {
        colunaEdicao.setCellValueFactory(new PropertyValueFactory<>("edicao"));
        colunaLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colunaGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colunaDistancia.setCellValueFactory(new PropertyValueFactory<>("distancia"));
        colunaPodio.setCellValueFactory(new PropertyValueFactory<>("podio"));
        colunaTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));

        colunaAtleta.setCellFactory(col -> new TableCell<Resultado, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    Resultado resultado = (Resultado) getTableRow().getItem();
                    int atletaId = resultado.getAtletaId();
                    String nomeAtleta = buscarNomeAtleta(atletaId); // crie essa função
                    setText(nomeAtleta);
                }
            }
        });

        tabelaCorridas.setItems(corridasList);
        tabelaResultados.setItems(resultadosList);

        tabelaCorridas.setOnMouseClicked(this::corridaSelecionada);
    }

    public void carregarCorridas() {
        corridasList.clear();
        corridasList.addAll(corridaDAO.listar());
    }

    private void corridaSelecionada(MouseEvent event) {
        Corrida corridaSelecionada = tabelaCorridas.getSelectionModel().getSelectedItem();
        if (corridaSelecionada != null) {
            carregarResultados(corridaSelecionada.getId());
        }
    }

    private void carregarResultados(int corridaId) {
        try {
            resultadosList.clear();
            for (Resultado r : resultadoDAO.listarTodos()) {
                if (r.getCorridaId() == corridaId) {
                    resultadosList.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void inserirResultado() {
        Corrida corridaSelecionada = tabelaCorridas.getSelectionModel().getSelectedItem();
        if (corridaSelecionada != null) {
            abrirDialogResultado(null, corridaSelecionada.getId());
        } else {
            alertar("Selecione uma corrida antes de inserir um resultado.");
        }
    }

    @FXML
    private void editarResultado() {
        Resultado resultadoSelecionado = tabelaResultados.getSelectionModel().getSelectedItem();
        if (resultadoSelecionado != null) {
            abrirDialogResultado(resultadoSelecionado, resultadoSelecionado.getCorridaId());
        } else {
            alertar("Selecione um resultado para editar.");
        }
    }

    @FXML
    private void excluirResultado() {
        Resultado resultadoSelecionado = tabelaResultados.getSelectionModel().getSelectedItem();
        if (resultadoSelecionado != null) {
            try {
                resultadoDAO.deletar(resultadoSelecionado.getId());
                resultadosList.remove(resultadoSelecionado);
            } catch (SQLException e) {
                e.printStackTrace();
                alertar("Erro ao excluir resultado.");
            }
        } else {
            alertar("Selecione um resultado para excluir.");
        }
    }

    private String buscarNomeAtleta(int atletaId) {
        if (cacheNomesAtletas.containsKey(atletaId)) {
            return cacheNomesAtletas.get(atletaId);
        }

        String sql = "SELECT nome FROM Atleta WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, atletaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                cacheNomesAtletas.put(atletaId, nome);
                return nome;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Desconhecido";
    }

    @FXML
    public void handleButtonLimpar() {
        tabelaCorridas.getSelectionModel().clearSelection();
        tabelaResultados.getItems().clear();
        tabelaResultados.getSelectionModel().clearSelection();
    }

    private void abrirDialogResultado(Resultado resultado, int corridaId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gerenciamentocorridas/view/FXMLResultadoDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(resultado == null ? "Inserir Resultado" : "Editar Resultado");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tabelaResultados.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            FXMLResultadoDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setConnection(this.connection);
            controller.setCorridaId(corridaId);
            controller.setResultado(resultado);

            dialogStage.showAndWait();

            if (controller.isConfirmado()) {
                carregarResultados(corridaId); // atualiza a tabela após inserção/edição
            }

        } catch (IOException e) {
            e.printStackTrace();
            alertar("Erro ao abrir o diálogo de resultado.");
        }
    }

    private void alertar(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
