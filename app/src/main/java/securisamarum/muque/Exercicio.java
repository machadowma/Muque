package securisamarum.muque;

public class Exercicio {
    Integer id,id_plano_de_treinamento,id_tipo_exercicio,id_grupo_muscular;
    Integer dia_de_treinamento,posicao;
    Integer carga,series,repeticoes;
    Integer tempo_segundos,distancia_km;
    String nome;

    // Construtor para exercício de musculação
    public Exercicio(Integer id, Integer id_tipo_exercicio, Integer id_grupo_muscular,  Integer posicao, Integer carga, Integer series, Integer repeticoes, String nome) {
        this.id = id;
        this.id_tipo_exercicio = id_tipo_exercicio;
        this.id_grupo_muscular = id_grupo_muscular;
        this.posicao = posicao;
        this.carga = carga;
        this.series = series;
        this.repeticoes = repeticoes;
        this.nome = nome;
    }

    // Construtor para exercício de cárdio
    public Exercicio(Integer id, Integer id_tipo_exercicio, Integer id_grupo_muscular, Integer posicao, Integer tempo_segundos, Integer distancia_km, String nome) {
        this.id = id;
        this.id_tipo_exercicio = id_tipo_exercicio;
        this.id_grupo_muscular = id_grupo_muscular;
        this.posicao = posicao;
        this.tempo_segundos = tempo_segundos;
        this.distancia_km = distancia_km;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_plano_de_treinamento() {
        return id_plano_de_treinamento;
    }

    public void setId_plano_de_treinamento(Integer id_plano_de_treinamento) {
        this.id_plano_de_treinamento = id_plano_de_treinamento;
    }

    public Integer getDia_de_treinamento() {
        return dia_de_treinamento;
    }

    public void setDia_de_treinamento(Integer dia_de_treinamento) {
        this.dia_de_treinamento = dia_de_treinamento;
    }

    public Integer getId_tipo_exercicio() {
        return id_tipo_exercicio;
    }

    public void setId_tipo_exercicio(Integer id_tipo_exercicio) {
        this.id_tipo_exercicio = id_tipo_exercicio;
    }

    public Integer getId_grupo_muscular() {
        return id_grupo_muscular;
    }

    public String getStr_grupo_muscular() {
        String str;
        switch(id_grupo_muscular) {
            case 1:
                str = "Abdômen";
                break;
            case 2:
                str = "Membros Inferiores";
                break;
            case 3:
                str = "Peito";
                break;
            case 4:
                str = "Tríceps";
                break;
            case 5:
                str = "Bíceps";
                break;
            case 6:
                str = "Costas";
                break;
            case 7:
                str = "Ombro";
                break;
            case 8:
                str = "Trapézio";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    public void setId_grupo_muscular(Integer id_grupo_muscular) {
        this.id_grupo_muscular = id_grupo_muscular;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public Integer getCarga() {
        return carga;
    }

    public void setCarga(Integer carga) {
        this.carga = carga;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(Integer repeticoes) {
        this.repeticoes = repeticoes;
    }

    public Integer getTempo_segundos() {
        return tempo_segundos;
    }

    public void setTempo_segundos(Integer tempo_segundos) {
        this.tempo_segundos = tempo_segundos;
    }

    public Integer getDistancia_km() {
        return distancia_km;
    }

    public void setDistancia_km(Integer distancia_km) {
        this.distancia_km = distancia_km;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
