package br.com.fiap.abrigue.model.entity;

import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ABRIGOS")
public class Abrigo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100)
    private String nome;

    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;

    @NotNull(message = "Capacidade máxima é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser maior que 0")
    private Integer capacidadeMaxima;

    private Integer vagasOcupadas = 0;

    @Enumerated(EnumType.STRING)
    private StatusAbrigo status; // ATIVO, INATIVO, LOTADO

    private String responsavel;
    private String telefone;

    @OneToMany(mappedBy = "abrigo", cascade = CascadeType.ALL)
    private List<Pessoa> pessoas = new ArrayList<>();

    @OneToMany(mappedBy = "abrigo", cascade = CascadeType.ALL)
    private List<Recurso> recursos = new ArrayList<>();

    public Abrigo() {
    }

    public Abrigo(Long id, String nome, String endereco, Integer capacidadeMaxima, Integer vagasOcupadas, StatusAbrigo status, String responsavel, String telefone, List<Pessoa> pessoas, List<Recurso> recursos) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidadeMaxima = capacidadeMaxima;
        this.vagasOcupadas = vagasOcupadas;
        this.status = status;
        this.responsavel = responsavel;
        this.telefone = telefone;
        this.pessoas = pessoas;
        this.recursos = recursos;
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

    public StatusAbrigo getStatus() {
        return status;
    }

    public void setStatus(StatusAbrigo status) {
        this.status = status;
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

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }
}