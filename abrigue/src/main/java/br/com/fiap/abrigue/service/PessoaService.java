package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.entity.Pessoa;
import br.com.fiap.abrigue.model.enums.SituacaoEspecial;
import br.com.fiap.abrigue.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    private final AbrigoService abrigoService;

    public PessoaService(PessoaRepository pessoaRepository, AbrigoService abrigoService) {
        this.pessoaRepository = pessoaRepository;
        this.abrigoService = abrigoService;
    }

    public List<Pessoa> listarTodas() {
        return pessoaRepository.findAll();
    }

    public List<Pessoa> listarPorAbrigo(Long abrigoId) {
        return pessoaRepository.findByAbrigoId(abrigoId);
    }

    public Optional<Pessoa> buscarPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    public Optional<Pessoa> buscarPorCpf(String cpf) {
        return pessoaRepository.findByCpf(cpf);
    }

    public Pessoa salvar(Pessoa pessoa) {

        if (pessoa.getDataEntrada() == null) {
            pessoa.setDataEntrada(new Date());
        }

        if (pessoa.getSituacaoEspecial() == null) {
            pessoa.setSituacaoEspecial(SituacaoEspecial.NENHUMA);
        }

        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        if (pessoaSalva.getAbrigo() != null) {
            atualizarVagasAbrigo(pessoaSalva.getAbrigo().getId());
        }

        return pessoaSalva;
    }

    public void deletar(Long id) {
        Optional<Pessoa> pessoa = buscarPorId(id);
        if (pessoa.isPresent()) {
            Long abrigoId = pessoa.get().getAbrigo().getId();
            pessoaRepository.deleteById(id);
            atualizarVagasAbrigo(abrigoId);
        }
    }

    public List<Pessoa> buscarPorNome(String nome) {
        return pessoaRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Pessoa> listarPorSituacaoEspecial(SituacaoEspecial situacaoEspecial) {
        return pessoaRepository.findBySituacaoEspecial(situacaoEspecial);
    }

    private void atualizarVagasAbrigo(Long abrigoId) {
        Long totalPessoas = pessoaRepository.contarPessoasPorAbrigo(abrigoId);
        Optional<Abrigo> abrigo = abrigoService.buscarPorId(abrigoId);

        if (abrigo.isPresent()) {
            abrigo.get().setVagasOcupadas(totalPessoas.intValue());
            abrigoService.salvar(abrigo.get());
        }
    }

    public boolean cpfJaExiste(String cpf, Long idPessoa) {
        Optional<Pessoa> pessoa = buscarPorCpf(cpf);
        return pessoa.isPresent() && !pessoa.get().getId().equals(idPessoa);
    }
}
