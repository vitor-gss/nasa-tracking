package com.rastreadorespacial.padroes.comportamentais.observer;

/**
 * <strong>Padrão comportamental: Observer — papel Observer.</strong>
 *
 * <p>Qualquer objeto interessado em saber quando um feed da NASA foi carregado (ou falhou) implementa esta
 * interface e se inscreve no sujeito ({@link com.rastreadorespacial.padroes.estruturais.facade.MenuContext}). O sujeito não conhece
 * as classes concretas: só chama {@link #aoAtualizarFeed(EventoFeed)} em cada inscrito.</p>
 *
 * <p>Por ter um único método, um inscrito também pode ser um lambda.</p>
 */
@FunctionalInterface
public interface FeedObserver {

    /**
     * Chamado sempre que uma fonte de dados termina de carregar, com sucesso ou com erro.
     *
     * @param evento o que aconteceu
     */
    void aoAtualizarFeed(EventoFeed evento);
}
