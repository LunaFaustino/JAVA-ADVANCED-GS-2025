package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.dto.AbrigoCapacidadeBaixaMessage;

import java.util.Map;

public interface MessageConsumerService {

    void processarAbrigoCapacidadeBaixa(AbrigoCapacidadeBaixaMessage message);

    void processarAlertaCritico(Map<String, Object> message);

    void processarRecursoEstoqueBaixo(Map<String, Object> message);
}