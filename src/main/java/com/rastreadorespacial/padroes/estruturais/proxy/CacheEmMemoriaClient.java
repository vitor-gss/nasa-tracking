package com.rastreadorespacial.padroes.estruturais.proxy;

import com.rastreadorespacial.api.SpaceApiException;
import com.rastreadorespacial.api.SpaceDataClient;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * <strong>Padrão estrutural: Proxy (proxy de cache).</strong>
 *
 * <p>Implementa a <em>mesma interface</em> do objeto real ({@link SpaceDataClient}) e fica no lugar dele.
 * Ele decide <em>se</em> a chamada chega ao cliente HTTP real: se a última resposta ainda está dentro da
 * validade, devolve a cópia em memória e nenhuma requisição é feita. Isso protege o limite de requisições
 * da NASA (RF8) e é invisível para quem chama: o código cliente continua chamando apenas {@code fetchRaw()}.</p>
 *
 * <ul>
 *   <li><b>Subject:</b> {@link SpaceDataClient}</li>
 *   <li><b>RealSubject:</b> {@link NeoWsClient}, {@link ApodClient}, {@link CelestrakClient}, {@link SbdbClient}</li>
 *   <li><b>Proxy:</b> esta classe</li>
 * </ul>
 */
public final class CacheEmMemoriaClient implements SpaceDataClient {

    public static final Duration VALIDADE_PADRAO = Duration.ofMinutes(1);

    private final SpaceDataClient real;
    private final Duration validade;
    private final Clock relogio;

    private String ultimaResposta;
    private Instant obtidaEm;
    private int chamadasReais;

    public CacheEmMemoriaClient(SpaceDataClient real) {
        this(real, VALIDADE_PADRAO, Clock.systemUTC());
    }

    public CacheEmMemoriaClient(SpaceDataClient real, Duration validade, Clock relogio) {
        this.real = Objects.requireNonNull(real, "O cliente real não pode ser nulo.");
        this.validade = Objects.requireNonNull(validade, "A validade não pode ser nula.");
        this.relogio = Objects.requireNonNull(relogio, "O relógio não pode ser nulo.");
    }

    @Override
    public String fetchRaw() throws SpaceApiException {
        Instant agora = relogio.instant();
        if (ultimaResposta != null && agora.isBefore(obtidaEm.plus(validade))) {
            return ultimaResposta; // controla o acesso: o objeto real nem é chamado
        }
        String resposta = real.fetchRaw();
        chamadasReais++;
        ultimaResposta = resposta;
        obtidaEm = agora;
        return resposta;
    }

    /**
     * Quantas vezes o cliente real (rede) foi de fato acionado. Útil para demonstrar o proxy.
     *
     * @return número de chamadas reais
     */
    public int chamadasReais() {
        return chamadasReais;
    }
}
