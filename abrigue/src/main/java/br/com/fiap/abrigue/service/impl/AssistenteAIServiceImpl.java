package br.com.fiap.abrigue.service.impl;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.entity.Pessoa;
import br.com.fiap.abrigue.model.entity.Recurso;
import br.com.fiap.abrigue.service.AbrigoService;
import br.com.fiap.abrigue.service.AssistenteAIService;
import br.com.fiap.abrigue.service.PessoaService;
import br.com.fiap.abrigue.service.RecursoService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssistenteAIServiceImpl implements AssistenteAIService {

    private final ChatClient chatClient;
    private final AbrigoService abrigoService;
    private final PessoaService pessoaService;
    private final RecursoService recursoService;

    public AssistenteAIServiceImpl(ChatClient.Builder chatClientBuilder,
                                   AbrigoService abrigoService,
                                   PessoaService pessoaService,
                                   RecursoService recursoService) {
        this.chatClient = chatClientBuilder.build();
        this.abrigoService = abrigoService;
        this.pessoaService = pessoaService;
        this.recursoService = recursoService;
    }

    @Override
    public String analisarAbrigo(Long abrigoId) {
        try {
            Abrigo abrigo = abrigoService.buscarPorId(abrigoId)
                    .orElseThrow(() -> new RuntimeException("Abrigo não encontrado"));

            List<Pessoa> pessoas = pessoaService.listarPorAbrigo(abrigoId);
            List<Recurso> recursos = recursoService.listarPorAbrigo(abrigoId);

            String contexto = montarContextoAnalise(abrigo, pessoas, recursos);

            String prompt = """
                Você é um assistente especializado em gerenciamento de abrigos emergenciais.
                
                Analise os dados fornecidos e forneça:
                1. Avaliação da situação atual do abrigo
                2. Identificação de recursos críticos ou insuficientes
                3. Recomendações específicas para melhorar a gestão
                4. Alertas sobre possíveis problemas
                5. Sugestões de prioridades de ação
                
                Seja objetivo e prático nas recomendações.
                
                Dados do abrigo:
                """ + contexto;

            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

        } catch (Exception e) {
            return "Erro ao processar análise: " + e.getMessage();
        }
    }

    @Override
    public String analisarTodosAbrigos() {
        try {
            List<Abrigo> abrigos = abrigoService.listarTodos();

            if (abrigos.isEmpty()) {
                return "Nenhum abrigo cadastrado no sistema.";
            }

            String contextoGeral = montarContextoGeral(abrigos);

            String prompt = """
                Você é um especialista em gestão de abrigos emergenciais.
                
                Analise o panorama geral do sistema de abrigos e forneça:
                1. Resumo da situação atual do sistema
                2. Abrigos que precisam de atenção prioritária
                3. Recursos mais escassos no sistema
                4. Recomendações estratégicas
                5. Plano de ação para otimização
                
                Dados do sistema:
                """ + contextoGeral;

            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

        } catch (Exception e) {
            return "Erro ao processar análise geral: " + e.getMessage();
        }
    }

    private String montarContextoAnalise(Abrigo abrigo, List<Pessoa> pessoas, List<Recurso> recursos) {
        StringBuilder contexto = new StringBuilder();

        contexto.append("ABRIGO: ").append(abrigo.getNome()).append("\n");
        contexto.append("Capacidade máxima: ").append(abrigo.getCapacidadeMaxima()).append("\n");
        contexto.append("Vagas ocupadas: ").append(abrigo.getVagasOcupadas()).append("\n");
        contexto.append("Vagas disponíveis: ").append(abrigo.getCapacidadeMaxima() - abrigo.getVagasOcupadas()).append("\n");
        contexto.append("Status: ").append(abrigo.getStatus().getDescricao()).append("\n");
        contexto.append("Taxa de ocupação: ").append(String.format("%.1f%%",
                abrigoService.calcularPercentualOcupacao(abrigo))).append("\n\n");

        if (!pessoas.isEmpty()) {
            contexto.append("PESSOAS ABRIGADAS (").append(pessoas.size()).append(" total):\n");

            Map<String, Long> situacoesEspeciais = pessoas.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getSituacaoEspecial().getDescricao(),
                            Collectors.counting()));

            situacoesEspeciais.forEach((situacao, count) ->
                    contexto.append("- ").append(situacao).append(": ").append(count).append("\n"));

            double idadeMedia = pessoas.stream()
                    .filter(p -> p.getIdade() != null)
                    .mapToInt(Pessoa::getIdade)
                    .average()
                    .orElse(0);
            contexto.append("- Idade média: ").append(String.format("%.1f anos", idadeMedia)).append("\n\n");
        }

        if (!recursos.isEmpty()) {
            contexto.append("RECURSOS DISPONÍVEIS:\n");

            Map<String, List<Recurso>> recursosPorTipo = recursos.stream()
                    .collect(Collectors.groupingBy(r -> r.getTipo().getDescricao()));

            recursosPorTipo.forEach((tipo, listaRecursos) -> {
                int totalQuantidade = listaRecursos.stream().mapToInt(Recurso::getQuantidade).sum();
                long recursosComEstoqueBaixo = listaRecursos.stream()
                        .filter(r -> r.getQuantidade() <= 10)
                        .count();

                contexto.append("- ").append(tipo).append(": ").append(totalQuantidade)
                        .append(" unidades totais");
                if (recursosComEstoqueBaixo > 0) {
                    contexto.append(" (").append(recursosComEstoqueBaixo).append(" com estoque baixo)");
                }
                contexto.append("\n");
            });
        } else {
            contexto.append("RECURSOS: Nenhum recurso cadastrado\n");
        }

        return contexto.toString();
    }

    private String montarContextoGeral(List<Abrigo> abrigos) {
        StringBuilder contexto = new StringBuilder();

        int totalCapacidade = abrigos.stream().mapToInt(Abrigo::getCapacidadeMaxima).sum();
        int totalOcupado = abrigos.stream().mapToInt(Abrigo::getVagasOcupadas).sum();
        int totalDisponivel = totalCapacidade - totalOcupado;

        contexto.append("PANORAMA GERAL DO SISTEMA:\n");
        contexto.append("Total de abrigos: ").append(abrigos.size()).append("\n");
        contexto.append("Capacidade total: ").append(totalCapacidade).append(" vagas\n");
        contexto.append("Vagas ocupadas: ").append(totalOcupado).append("\n");
        contexto.append("Vagas disponíveis: ").append(totalDisponivel).append("\n");
        contexto.append("Taxa de ocupação geral: ").append(
                String.format("%.1f%%", totalCapacidade > 0 ? (double) totalOcupado * 100 / totalCapacidade : 0)
        ).append("\n\n");

        contexto.append("DETALHES POR ABRIGO:\n");
        for (Abrigo abrigo : abrigos) {
            double ocupacao = abrigoService.calcularPercentualOcupacao(abrigo);
            contexto.append("- ").append(abrigo.getNome())
                    .append(": ").append(abrigo.getVagasOcupadas())
                    .append("/").append(abrigo.getCapacidadeMaxima())
                    .append(" (").append(String.format("%.1f%%", ocupacao)).append(")")
                    .append(" - Status: ").append(abrigo.getStatus().getDescricao())
                    .append("\n");
        }

        return contexto.toString();
    }
}