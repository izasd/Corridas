package gerenciamentocorridas.model.domain;

public class Atleta {
    private int id;
    private String nome;
    private int idade;
    private char genero; // 'M' ou 'F'
    private String pais;

    public Atleta() {}

    public Atleta(int id, String nome, int idade, char genero, String pais) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.genero = genero;
        this.pais = pais;
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

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}

