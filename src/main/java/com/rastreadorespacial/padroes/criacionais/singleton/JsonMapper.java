package com.rastreadorespacial.padroes.criacionais.singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <strong>Padrão criacional: Singleton (enum singleton).</strong>
 *
 * <p>Antes, cinco classes criavam o seu próprio {@code new ObjectMapper()} (sinal de alerta do slide
 * "um recurso vira múltiplas instâncias por engano"). O {@link ObjectMapper} é caro de criar e é
 * thread-safe depois de configurado, então o sistema inteiro passa a compartilhar uma única instância.</p>
 *
 * <p>Foi usada a forma recomendada por Joshua Bloch (Effective Java): um {@code enum} com uma única
 * constante {@link #INSTANCE}. Ela é thread-safe e imune a reflexão e serialização sem código extra.</p>
 *
 * <pre>{@code
 * JsonNode raiz = JsonMapper.INSTANCE.mapper().readTree(json);
 * }</pre>
 */
public enum JsonMapper {
    INSTANCE;

    private final ObjectMapper mapper;

    JsonMapper() {
        // Criado uma única vez, na primeira referência ao enum.
        this.mapper = new ObjectMapper();
    }

    /**
     * Retorna o {@link ObjectMapper} compartilhado por toda a aplicação.
     *
     * @return a única instância de ObjectMapper
     */
    public ObjectMapper mapper() {
        return mapper;
    }
}
