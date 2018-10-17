package securisamarum.muque;

public class Plano {
    private Integer id, posicao;
    private String nome;

    public Plano(Integer id, String nome, Integer posicao) {
        this.id = id;
        this.nome = nome;
        this.posicao = posicao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer id) {
        this.posicao = posicao;
    }
}
