package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.entity.Recurso;

public interface MessagePublisherService {

    void enviarMensagemAbrigoCapacidadeBaixa(Abrigo abrigo);

    void enviarMensagemRecursoEstoqueBaixo(Recurso recurso);

    void enviarMensagem(String fila, Object mensagem);
}