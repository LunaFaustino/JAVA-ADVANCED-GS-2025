package br.com.fiap.abrigue.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

@Entity
@Table(name = "PESSOAS")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
            message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    @Min(value = 0, message = "Idade deve ser positiva")
    private Integer idade;

    private String telefone;

    @Enumerated(EnumType.STRING)
    private SituacaoEspecial situacaoEspecial;

    @ManyToOne
    @JoinColumn(name = "abrigo_id")
    private Abrigo abrigo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;

    public Pessoa() {
    }

    public Pessoa(Long id, String nome, String cpf, Integer idade, String telefone, SituacaoEspecial situacaoEspecial, Abrigo abrigo, Date dataEntrada) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.telefone = telefone;
        this.situacaoEspecial = situacaoEspecial;
        this.abrigo = abrigo;
        this.dataEntrada = dataEntrada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public SituacaoEspecial getSituacaoEspecial() {
        return situacaoEspecial;
    }

    public void setSituacaoEspecial(SituacaoEspecial situacaoEspecial) {
        this.situacaoEspecial = situacaoEspecial;
    }

    public Abrigo getAbrigo() {
        return abrigo;
    }

    public void setAbrigo(Abrigo abrigo) {
        this.abrigo = abrigo;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
}
