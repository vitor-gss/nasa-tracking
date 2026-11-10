/**
 * <b>Proxy (de cache).</b> CacheEmMemoriaClient implementa a mesma interface do cliente real e decide se a
 * chamada chega à rede.
 *
 * <p><b>Status: EM USO.</b> Criado em FonteDeDados.cliente() para todas as fontes. Visível no terminal: ao
 * atualizar os feeds duas vezes em menos de 1 minuto, a segunda linha de log mostra "em 0 ms".</p>
 */
package com.rastreadorespacial.padroes.estruturais.proxy;
