package br.com.fiap.abrigue.model.enums;

public enum StatusAbrigo {

    ATIVO("Ativo"),
    INATIVO("Inativo"),
    LOTADO("Lotado"),
    MANUTENCAO("Em Manutenção");

    private final String descricao;

    StatusAbrigo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
