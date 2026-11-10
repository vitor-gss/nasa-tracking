package com.rastreadorespacial.padroes.comportamentais.observer;

/**
 * Notificação enviada aos {@link FeedObserver} (padrão Observer) quando uma fonte termina de carregar.
 *
 * @param fonte      nome da fonte ("neows", "apod", "celestrak", "sbdb")
 * @param sucesso    true se os dados foram carregados (da API ou do cache)
 * @param mensagem   texto legível descrevendo o resultado
 * @param quantidade quantos objetos espaciais foram interpretados
 * @param doCache    true se os dados vieram do cache em disco
 */
public record EventoFeed(String fonte, boolean sucesso, String mensagem, int quantidade, boolean doCache) {
}
