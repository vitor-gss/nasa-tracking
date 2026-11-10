package com.rastreadorespacial.padroes.comportamentais.observer;

import java.io.PrintStream;
import java.util.Objects;

/**
 * <strong>Padrão comportamental: Observer — ConcreteObserver.</strong>
 *
 * <p>Imprime no terminal o resultado de cada carga de feed. Antes, essas mensagens eram impressas
 * diretamente dentro do contexto do menu; agora o contexto só "publica" o evento e quem estiver inscrito
 * decide o que fazer com ele.</p>
 */
public final class ConsoleFeedObserver implements FeedObserver {

    private final PrintStream saida;

    public ConsoleFeedObserver(PrintStream saida) {
        this.saida = Objects.requireNonNull(saida, "A saída não pode ser nula.");
    }

    @Override
    public void aoAtualizarFeed(EventoFeed evento) {
        saida.println(evento.mensagem());
    }
}
