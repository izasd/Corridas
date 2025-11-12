package sockets.thread;

/**
 *
 * @author Iza
 */

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClienteSistemaGruposController {

    @FXML
    private TableView<sockets.thread.ContadorGrupo> tableViewGeral;
    @FXML
    private TableColumn<sockets.thread.ContadorGrupo, Integer> tableColumnPosicao;
    @FXML
    private TableColumn<sockets.thread.ContadorGrupo, String> tableColumnGrupo;
    @FXML
    private TableColumn<sockets.thread.ContadorGrupo, Integer> tableColumnUtilizacoes;
    @FXML
    private Label labelLog;

    private final int idGrupo = 4;
    private final String servidor = "34.41.27.130";
    private final int porta = 12345;

    @FXML
    public void initialize() {
        tableColumnPosicao.setCellValueFactory(new PropertyValueFactory<>("idGrupo"));
        tableColumnGrupo.setCellValueFactory(new PropertyValueFactory<>("nomeGrupo"));
        tableColumnUtilizacoes.setCellValueFactory(new PropertyValueFactory<>("quantidadeUtilizacoes"));
        new Thread(this::conectarComServidor).start();
    }

    private void conectarComServidor() {
        try (Socket clienteSocket = new Socket(servidor, porta)) {
            System.out.println("✅ Conectado ao servidor: " + servidor + ":" + porta);

            ObjectOutputStream saida = new ObjectOutputStream(clienteSocket.getOutputStream());
            saida.writeObject(idGrupo);

            ObjectInputStream entrada = new ObjectInputStream(clienteSocket.getInputStream());

            @SuppressWarnings("unchecked")
            List<sockets.thread.ContadorGrupo> ranking = (List<sockets.thread.ContadorGrupo>) entrada.readObject();

            @SuppressWarnings("unchecked")
            List<sockets.thread.LogGrupo> logs = (List<sockets.thread.LogGrupo>) entrada.readObject();

            Platform.runLater(() -> {
                tableViewGeral.getItems().setAll(ranking);
            }); 
            atualizarLog(logs);
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> labelLog.setText("Erro ao conectar: " + e.getMessage()));
        }
    }
    
    private void atualizarLog(List<LogGrupo> logs) {
        if (logs.isEmpty()) {
            Platform.runLater(() -> labelLog.setText("Nenhum log encontrado: "));
            return;
        }
        Thread thread = new Thread(() -> {
            try {
                while(true){
                    for(LogGrupo i : logs){
                        String brasilTimestamp = convertToBrasiliaTime(i.getTimestamp());
                        Platform.runLater(() -> {
                            labelLog.setText(brasilTimestamp);
                        });
                        Thread.sleep(2000);
                    }
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    
    private String convertToBrasiliaTime(String timestamp) {
        try {
            // Formato que vem do servidor: "yyyy-MM-dd HH:mm:ss"
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(timestamp, inputFormatter);
            
            // Supondo que o horário do servidor está em UTC
            ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.of("UTC"));
            
            // Convertendo para o fuso horário de Brasília
            ZonedDateTime brasiliaDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));
            
            // Formato que queremos exibir na tela: "dd/MM/yyyy 'às' HH:mm:ss"
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");
            return brasiliaDateTime.format(outputFormatter);
        } catch (Exception e) {
            // Se houver qualquer erro na conversão, retorna o timestamp original
            System.err.println("Não foi possível converter o horário para o fuso de Brasília: " + e.getMessage());
            return timestamp;
        }
    }
}

