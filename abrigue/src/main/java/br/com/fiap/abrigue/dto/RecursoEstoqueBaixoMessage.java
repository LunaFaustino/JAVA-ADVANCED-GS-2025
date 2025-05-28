package br.com.fiap.abrigue.dto;

import java.time.LocalDateTime;

public class RecursoEstoqueBaixoMessage {

    private Long recursoId;
    private String nomeRecurso;
    private Integer quantidade;
    private String tipoRecurso;
    private Long abrigoId;
    private String nomeAbrigo;
    private LocalDateTime dataHora;

    public RecursoEstoqueBaixoMessage() {
        this.dataHora = LocalDateTime.now();
    }

    public RecursoEstoqueBaixoMessage(Long recursoId, String nomeRecurso, Integer quantidade, String tipoRecurso, Long abrigoId, String nomeAbrigo) {
        this();
        this.recursoId = recursoId;
        this.nomeRecurso = nomeRecurso;
        this.quantidade = quantidade;
        this.tipoRecurso = tipoRecurso;
        this.abrigoId = abrigoId;
        this.nomeAbrigo = nomeAbrigo;
    }

    public Long getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(Long recursoId) {
        this.recursoId = recursoId;
    }

    public String getNomeRecurso() {
        return nomeRecurso;
    }

    public void setNomeRecurso(String nomeRecurso) {
        this.nomeRecurso = nomeRecurso;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getTipoRecurso() {
        return tipoRecurso;
    }

    public void setTipoRecurso(String tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }

    public Long getAbrigoId() {
        return abrigoId;
    }

    public void setAbrigoId(Long abrigoId) {
        this.abrigoId = abrigoId;
    }

    public String getNomeAbrigo() {
        return nomeAbrigo;
    }

    public void setNomeAbrigo(String nomeAbrigo) {
        this.nomeAbrigo = nomeAbrigo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "RecursoEstoqueBaixoMessage{" +
                "recursoId=" + recursoId +
                ", nomeRecurso='" + nomeRecurso + '\'' +
                ", quantidade=" + quantidade +
                ", tipoRecurso='" + tipoRecurso + '\'' +
                ", abrigoId=" + abrigoId +
                ", nomeAbrigo='" + nomeAbrigo + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }
}
