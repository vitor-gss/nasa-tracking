package com.rastreadorespacial.padroes.criacionais.factorymethod;

import com.rastreadorespacial.cache.CachedDataResult;

/**
 * Resultado do Template Method {@link FonteDeDados#carregar}.
 *
 * @param fonte      nome da fonte
 * @param dados      resposta bruta e metadados (API ou cache, tentativas)
 * @param quantidade número de objetos espaciais interpretados
 * @param mensagem   descrição legível do resultado
 */
public record ResultadoCarga(String fonte, CachedDataResult dados, int quantidade, String mensagem) {
}
