package br.com.fiap.abrigue.model.enums;

public enum TipoRecurso {

    ALIMENTO("Alimento"),
    MEDICAMENTO("Medicamento"),
    ROUPA("Roupa"),
    HIGIENE("Higiene"),
    OUTROS("Outros");

    private final String descricao;

    TipoRecurso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
