package gerenciamentocorridas.model.domain;

public class Corrida {
    private int id;
    private String edicao;
    private String local;
    private String categoria;
    private char genero; // 'M' ou 'F'
    private double distancia;
    private int qtdMaxCorr;
    private int qtdMinCorr;

    public Corrida() {}

    public Corrida(int id, String edicao, String local, String categoria, char genero, double distancia, int qtdMaxCorr, int qtdMinCorr) {
        this.id = id;
        this.edicao = edicao;
        this.local = local;
        this.categoria = categoria;
        this.genero = genero;
        this.distancia = distancia;
        this.qtdMaxCorr = qtdMaxCorr;
        this.qtdMinCorr = qtdMinCorr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getQtdMaxCorr() {
        return qtdMaxCorr;
    }

    public void setQtdMaxCorr(int qtdMaxCorr) {
        this.qtdMaxCorr = qtdMaxCorr;
    }

    public int getQtdMinCorr() {
        return qtdMinCorr;
    }

    public void setQtdMinCorr(int qtdMinCorr) {
        this.qtdMinCorr = qtdMinCorr;
    }
}

