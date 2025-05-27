package br.com.fiap.abrigue.model.enums;

public enum SituacaoEspecial {

    NENHUMA("Nenhuma"),
    DEFICIENCIA("Pessoa com Deficiência"),
    IDOSO("Idoso"),
    CRIANCA("Criança"),
    GESTANTE("Gestante"),
    DOENCA_CRONICA("Doença Crônica");

    private final String descricao;

    SituacaoEspecial(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
