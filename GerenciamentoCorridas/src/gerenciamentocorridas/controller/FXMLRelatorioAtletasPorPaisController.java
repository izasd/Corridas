package gerenciamentocorridas.controller;

import gerenciamentocorridas.model.dao.AtletaDAO;
import gerenciamentocorridas.model.database.Database;
import gerenciamentocorridas.model.database.DatabaseFactory;
import gerenciamentocorridas.model.domain.Atleta;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FXMLRelatorioAtletasPorPaisController implements Initializable {

    @FXML
    private TableView<Atleta> tableViewAtletas;
    @FXML
    private TableColumn<Atleta, String> tableColumnAtletaNome;
    @FXML
    private TableColumn<Atleta, Integer> tableColumnAtletaIdade;
    @FXML
    private TableColumn<Atleta, String> tableColumnAtletaGenero;
    @FXML
    private TableColumn<Atleta, String> tableColumnAtletaPais;
    @FXML
    private TableColumn<Atleta, Integer> tableColumnAtletaBronze;
    @FXML
    private TableColumn<Atleta, Integer> tableColumnAtletaPrata;
    @FXML
    private TableColumn<Atleta, Integer> tableColumnAtletaOuro;
    @FXML
    private Button buttonImprimir;

    private List<Atleta> listAtletas;
    private ObservableList<Atleta> observableListAtletas;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final AtletaDAO atletaDAO = new AtletaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        atletaDAO.setConnection(connection);

        carregarTableViewAtletas();
    }

    public void carregarTableViewAtletas() {
        tableColumnAtletaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnAtletaIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        tableColumnAtletaGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        tableColumnAtletaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        tableColumnAtletaBronze.setCellValueFactory(new PropertyValueFactory<>("bronze"));
        tableColumnAtletaPrata.setCellValueFactory(new PropertyValueFactory<>("prata"));
        tableColumnAtletaOuro.setCellValueFactory(new PropertyValueFactory<>("ouro"));

        listAtletas = atletaDAO.listar();

        observableListAtletas = FXCollections.observableArrayList(listAtletas);
        tableViewAtletas.setItems(observableListAtletas);
    }

    @FXML
    public void handleImprimir() throws JRException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Filtro de País");
        dialog.setHeaderText("Gerar relatório de atletas por país");
        dialog.setContentText("Informe o país:");

        dialog.showAndWait().ifPresent(pais -> {
            try {
                Map<String, Object> parametros = new HashMap<>();
                parametros.put("pais", pais);

                URL url = getClass().getResource("/gerenciamentocorridas/relatorios/AtletasPorPais.jasper");
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);

                JasperViewer viewer = new JasperViewer(jasperPrint, false);
                viewer.setTitle("Relatório de Atletas por País");
                viewer.setVisible(true);

            } catch (JRException e) {
                e.printStackTrace();
            }
        });
    }

}
