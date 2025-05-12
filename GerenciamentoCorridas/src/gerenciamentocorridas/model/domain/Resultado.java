package gerenciamentocorridas.model.domain;

public class Resultado {
    private int id;
    private int atletaId;
    private int corridaId;
    private Integer podio;
    private String tempo;

    public Resultado() {}

    public Resultado(int id, int atletaId, int corridaId, Integer podio, String tempo) {
        this.id = id;
        this.atletaId = atletaId;
        this.corridaId = corridaId;
        this.podio = podio;
        this.tempo = tempo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAtletaId() {
        return atletaId;
    }

    public void setAtletaId(int atletaId) {
        this.atletaId = atletaId;
    }

    public int getCorridaId() {
        return corridaId;
    }

    public void setCorridaId(int corridaId) {
        this.corridaId = corridaId;
    }

    public Integer getPodio() {
        return podio;
    }

    public void setPodio(Integer podio) {
        this.podio = podio;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }
}
