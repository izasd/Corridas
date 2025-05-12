//Aluno: Izabelli

package gerenciamentocorridas.model.domain;

public class Corrida {
    private int id;
    private String edicao;
    private String pais;
    private String categoria;
    private double distancia;
    private String genero;
    private int qtdAtletas;
    private int qtdMin;
    private int qtdMax;

    public Corrida() {}

    public Corrida(int id, String edicao, String pais, String categoria, double distancia, String genero, int qtdAtletas, int qtdMin, int qtdMax) {
        this.id = id;
        this.edicao = edicao;
        this.pais = pais;
        this.categoria = categoria;
        this.distancia = distancia;
        this.genero = genero;
        this.qtdAtletas = qtdAtletas;
        this.qtdMin = qtdMin;
        this.qtdMax = qtdMax;
    }
    
    public Corrida (String edicao, String pais, String categoria, double distancia, String genero, int qtdAtletas, int qtdMin, int qtdMax) {
        this.edicao = edicao;
        this.pais = pais;
        this.categoria = categoria;
        this.distancia = distancia;
        this.genero = genero;
        this.qtdAtletas = qtdAtletas;
        this.qtdMin = qtdMin;
        this.qtdMax = qtdMax;
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getQtdAtletas() {
        return qtdAtletas;
    }

    public void setQtdAtletas(int qtdAtletas) {
        this.qtdAtletas = qtdAtletas;
    }

    public int getQtdMin() {
        return qtdMin;
    }

    public void setQtdMin(int qtdMin) {
        this.qtdMin = qtdMin;
    }

    public int getQtdMax() {
        return qtdMax;
    }

    public void setQtdMax(int qtdMax) {
        this.qtdMax = qtdMax;
    }
}

