package br.com.fiap.abrigue.model.entity;

import br.com.fiap.abrigue.model.enums.TipoRecurso;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "RECURSOS")
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do recurso é obrigatório")
    private String nome;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade deve ser positiva")
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    private TipoRecurso tipo;

    private String descricao;
    private Date dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "abrigo_id")
    private Abrigo abrigo;

    public Recurso() {
    }

    public Recurso(Long id, String nome, Integer quantidade, TipoRecurso tipo, String descricao, Date dataAtualizacao, Abrigo abrigo) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.descricao = descricao;
        this.dataAtualizacao = dataAtualizacao;
        this.abrigo = abrigo;
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public TipoRecurso getTipo() {
        return tipo;
    }

    public void setTipo(TipoRecurso tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Abrigo getAbrigo() {
        return abrigo;
    }

    public void setAbrigo(Abrigo abrigo) {
        this.abrigo = abrigo;
    }
}
