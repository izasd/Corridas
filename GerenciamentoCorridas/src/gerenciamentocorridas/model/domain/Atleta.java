//Aluno: Henrique
package gerenciamentocorridas.model.domain;

public class Atleta {

    private int id;
    private String nome;
    private int idade;
    private String genero;
    private String pais;
    private int bronze;
    private int prata;
    private int ouro;

    public Atleta() {
    }

    public Atleta(int id, String nome, int idade, String genero, String pais, int bronze, int prata, int ouro) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.genero = genero;
        this.pais = pais;
        this.bronze = bronze;
        this.prata = prata;
        this.ouro = ouro;
    }

    public Atleta(String nome, int idade, String genero, String pais, int bronze, int prata, int ouro) {
        this.nome = nome;
        this.idade = idade;
        this.genero = genero;
        this.pais = pais;
        this.bronze = bronze;
        this.prata = prata;
        this.ouro = ouro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public int getPrata() {
        return prata;
    }

    public void setPrata(int prata) {
        this.prata = prata;
    }

    public int getOuro() {
        return ouro;
    }

    public void setOuro(int ouro) {
        this.ouro = ouro;
    }

    @Override
    public String toString() {
        return nome;
    }

}
