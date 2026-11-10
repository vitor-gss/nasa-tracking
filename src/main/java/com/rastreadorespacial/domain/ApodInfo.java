package com.rastreadorespacial.domain;

/**
 * Metadados da imagem astronômica do dia (NASA APOD).
 *
 * @param titulo     título da imagem
 * @param data       data de publicação
 * @param explicacao descrição
 * @param url        URL da imagem (preferencialmente em alta resolução)
 */
public record ApodInfo(String titulo, String data, String explicacao, String url) {
}
