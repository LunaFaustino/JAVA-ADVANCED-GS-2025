package br.com.fiap.abrigue.dto;

import java.time.LocalDateTime;

public class PessoaCadastradaMessage {

    private Long pessoaId;
    private String nomePessoa;
    private String cpf;
    private Long abrigoId;
    private String nomeAbrigo;
    private LocalDateTime dataHora;

    public PessoaCadastradaMessage() {
        this.dataHora = LocalDateTime.now();
    }

    public PessoaCadastradaMessage(Long pessoaId, String nomePessoa, String cpf, Long abrigoId, String nomeAbrigo) {
        this();
        this.pessoaId = pessoaId;
        this.nomePessoa = nomePessoa;
        this.cpf = cpf;
        this.abrigoId = abrigoId;
        this.nomeAbrigo = nomeAbrigo;
    }

    public Long getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(Long pessoaId) {
        this.pessoaId = pessoaId;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
        return "PessoaCadastradaMessage{" +
                "pessoaId=" + pessoaId +
                ", nomePessoa='" + nomePessoa + '\'' +
                ", cpf='" + cpf + '\'' +
                ", abrigoId=" + abrigoId +
                ", nomeAbrigo='" + nomeAbrigo + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }
}

