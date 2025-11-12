package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.dao.ResultadoCorridaDAO;
import gerenciamentocorridas.model.domain.Atleta;
import gerenciamentocorridas.model.domain.Resultado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;
import java.util.List;

public class FXMLResultadoDialogController {

    @FXML
    private ComboBox<Atleta> comboAtleta;
    @FXML
    private TextField txtPosicao;
    @FXML
    private TextField txtTempo;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;

    private Stage dialogStage;
    private Resultado resultado;
    private boolean confirmado = false;
    private Connection connection;
    private ResultadoCorridaDAO resultadoDAO;
    private int corridaId;

    private ObservableList<Atleta> atletas = FXCollections.observableArrayList();

    public void setConnection(Connection connection) {
        this.connection = connection;
        this.resultadoDAO = new ResultadoCorridaDAO(connection);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setCorridaId(int corridaId) {
        this.corridaId = corridaId;
        carregarAtletasDaCorrida();
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;

        if (resultado != null) {
            for (Atleta atleta : atletas) {
                if (atleta.getId() == resultado.getAtletaId()) {
                    comboAtleta.setValue(atleta);
                    break;
                }
            }
            txtPosicao.setText(resultado.getPodio() != null ? resultado.getPodio().toString() : "");
            txtTempo.setText(resultado.getTempo());
        }
    }

    private void carregarAtletasDaCorrida() {
        try {
            String sql = "SELECT a.id, a.nome FROM atleta a "
                    + "JOIN Corrida_Atleta ca ON a.id = ca.atleta_id "
                    + "WHERE ca.corrida_id = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, corridaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Atleta atleta = new Atleta();
                atleta.setId(rs.getInt("id"));
                atleta.setNome(rs.getString("nome"));
                atletas.add(atleta);
            }

            comboAtleta.setItems(atletas);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao carregar atletas da corrida.");
        }
    }

    @FXML
    private void handleButtonConfirmar() {
        Atleta atletaSelecionado = comboAtleta.getValue();

        if (atletaSelecionado == null || txtTempo.getText().isEmpty()) {
            mostrarAlerta("Preencha todos os campos obrigatórios.");
            return;
        }

        try {
            Integer posicao = txtPosicao.getText().isEmpty() ? null : Integer.parseInt(txtPosicao.getText());
            String tempo = txtTempo.getText();

            // 1. Verifica se já existe outro atleta na mesma colocação (se preenchida)
            if (posicao != null) {
                String sql = "SELECT COUNT(*) FROM resultado_corrida WHERE corrida_id = ? AND podio = ? AND id <> ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, corridaId);
                    stmt.setInt(2, posicao);
                    stmt.setInt(3, (resultado != null && resultado.getId() != 0) ? resultado.getId() : -1);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        mostrarAlerta("Já existe um atleta nessa colocação.");
                        return;
                    }
                }
            }

            String sqlMinimo
                    = "SELECT COUNT(*) AS inscritos, c.qtd_min_corr "
                    + "FROM corrida_atleta ca "
                    + "JOIN corrida c ON c.id = ca.corrida_id "
                    + "WHERE ca.corrida_id = ? "
                    + "GROUP BY c.qtd_min_corr";

            try (PreparedStatement stmt = connection.prepareStatement(sqlMinimo)) {
                stmt.setInt(1, corridaId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int inscritos = rs.getInt("inscritos");
                    int minimo = rs.getInt("qtd_min_corr");

                    if (inscritos < minimo) {
                        mostrarAlerta("A corrida ainda não possui o número mínimo de atletas inscritos.");
                        return;
                    }
                } else {
                    mostrarAlerta("Não foi possível verificar o número de inscritos.");
                    return;
                }
            }

            if (resultado == null) {
                resultado = new Resultado();
            }

            resultado.setAtletaId(atletaSelecionado.getId());
            resultado.setCorridaId(corridaId);
            resultado.setPodio(posicao);
            resultado.setTempo(tempo);

            if (resultado.getId() == 0) {
                resultadoDAO.inserir(resultado);
            } else {
                resultadoDAO.atualizar(resultado);
            }

            confirmado = true;
            dialogStage.close();

        } catch (NumberFormatException e) {
            mostrarAlerta("Posição deve ser um número inteiro.");
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao salvar resultado.");
        }
    }

    @FXML
    private void handleButtonCancelar() {
        dialogStage.close();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
