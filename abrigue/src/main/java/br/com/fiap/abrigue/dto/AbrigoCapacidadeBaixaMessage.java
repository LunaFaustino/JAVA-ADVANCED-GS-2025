package br.com.fiap.abrigue.dto;

import java.time.LocalDateTime;

public class AbrigoCapacidadeBaixaMessage {

    private Long abrigoId;
    private String nomeAbrigo;
    private String endereco;
    private Integer capacidadeMaxima;
    private Integer vagasOcupadas;
    private Integer vagasDisponiveis;
    private Double percentualOcupacao;
    private String responsavel;
    private String telefone;
    private LocalDateTime dataHora;

    public AbrigoCapacidadeBaixaMessage() {
        this.dataHora = LocalDateTime.now();
    }

    public AbrigoCapacidadeBaixaMessage(Long abrigoId, String nomeAbrigo, String endereco,
                                        Integer capacidadeMaxima, Integer vagasOcupadas,
                                        String responsavel, String telefone) {
        this();
        this.abrigoId = abrigoId;
        this.nomeAbrigo = nomeAbrigo;
        this.endereco = endereco;
        this.capacidadeMaxima = capacidadeMaxima;
        this.vagasOcupadas = vagasOcupadas;
        this.vagasDisponiveis = capacidadeMaxima - vagasOcupadas;
        this.percentualOcupacao = capacidadeMaxima > 0 ? (double) vagasOcupadas / capacidadeMaxima * 100 : 0;
        this.responsavel = responsavel;
        this.telefone = telefone;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public Integer getVagasOcupadas() {
        return vagasOcupadas;
    }

    public void setVagasOcupadas(Integer vagasOcupadas) {
        this.vagasOcupadas = vagasOcupadas;
    }

    public Integer getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public void setVagasDisponiveis(Integer vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public Double getPercentualOcupacao() {
        return percentualOcupacao;
    }

    public void setPercentualOcupacao(Double percentualOcupacao) {
        this.percentualOcupacao = percentualOcupacao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "AbrigoCapacidadeBaixaMessage{" +
                "abrigoId=" + abrigoId +
                ", nomeAbrigo='" + nomeAbrigo + '\'' +
                ", endereco='" + endereco + '\'' +
                ", capacidadeMaxima=" + capacidadeMaxima +
                ", vagasOcupadas=" + vagasOcupadas +
                ", vagasDisponiveis=" + vagasDisponiveis +
                ", percentualOcupacao=" + percentualOcupacao +
                ", responsavel='" + responsavel + '\'' +
                ", telefone='" + telefone + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }
}
