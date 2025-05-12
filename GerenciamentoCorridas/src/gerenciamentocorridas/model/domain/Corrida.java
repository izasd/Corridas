//Aluno: Izabelli

package gerenciamentocorridas.model.domain;

import java.util.List;

public class Corrida {
    private int id;
    private String edicao;
    private String pais;
    private String categoria;
    private double distancia;
    private String genero;
    private int qtdAtletas;
    private int qtdMinCorr;
    private int qtdMaxCorr;
    private List<Atleta> atletas;

    public Corrida() {}

    public Corrida(int id, String edicao, String pais, String categoria, double distancia, String genero,
                   int qtdAtletas, int qtdMinCorr, int qtdMaxCorr, List<Atleta> atletas) {
        this.id = id;
        this.edicao = edicao;
        this.pais = pais;
        this.categoria = categoria;
        this.distancia = distancia;
        this.genero = genero;
        this.qtdAtletas = qtdAtletas;
        this.qtdMinCorr = qtdMinCorr;
        this.qtdMaxCorr = qtdMaxCorr;
        this.atletas = atletas;
    }

    public Corrida(String edicao, String pais, String categoria, double distancia, String genero,
                   int qtdAtletas, int qtdMinCorr, int qtdMaxCorr, List<Atleta> atletas) {
        this.edicao = edicao;
        this.pais = pais;
        this.categoria = categoria;
        this.distancia = distancia;
        this.genero = genero;
        this.qtdAtletas = qtdAtletas;
        this.qtdMinCorr = qtdMinCorr;
        this.qtdMaxCorr = qtdMaxCorr;
        this.atletas = atletas;
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
        return qtdMinCorr;
    }

    public void setQtdMin(int qtdMin) {
        this.qtdMinCorr = qtdMinCorr;
    }

    public int getQtdMax() {
        return qtdMaxCorr;
    }

    public void setQtdMax(int qtdMax) {
        this.qtdMaxCorr = qtdMaxCorr;
    }
    
    public List<Atleta> getAtletas() {
        return atletas;
    }

    public void setAtletas(List<Atleta> atletas) {
        this.atletas = atletas;
    }

}

