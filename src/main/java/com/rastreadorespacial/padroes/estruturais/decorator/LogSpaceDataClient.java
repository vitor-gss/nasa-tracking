package com.rastreadorespacial.padroes.estruturais.decorator;

import com.rastreadorespacial.api.SpaceApiException;
import com.rastreadorespacial.api.SpaceDataClient;

import java.io.PrintStream;
import java.util.Objects;

/**
 * <strong>Padrão estrutural: Decorator.</strong>
 *
 * <p>Recebe no construtor o <em>mesmo tipo</em> que implementa ({@link SpaceDataClient}) e acrescenta uma
 * responsabilidade — registrar no console quanto tempo cada busca levou e quantos bytes vieram — sem alterar
 * o cliente embrulhado. Como tem a mesma interface, pode ser empilhado com outros embrulhos:</p>
 *
 * <pre>{@code
 * SpaceDataClient neows = new LogSpaceDataClient("neows",
 *         new CacheEmMemoriaClient(new NeoWsClient()), System.out);
 * }</pre>
 *
 * <p>Diferença para o {@link CacheEmMemoriaClient} (Proxy): o Decorator <em>sempre</em> repassa a chamada e
 * soma comportamento; o Proxy <em>decide se</em> repassa.</p>
 */
public final class LogSpaceDataClient implements SpaceDataClient {

    private final String fonte;
    private final SpaceDataClient embrulhado;
    private final PrintStream saida;

    public LogSpaceDataClient(String fonte, SpaceDataClient embrulhado, PrintStream saida) {
        this.fonte = Objects.requireNonNull(fonte, "O nome da fonte não pode ser nulo.");
        this.embrulhado = Objects.requireNonNull(embrulhado, "O cliente embrulhado não pode ser nulo.");
        this.saida = Objects.requireNonNull(saida, "A saída não pode ser nula.");
    }

    @Override
    public String fetchRaw() throws SpaceApiException {
        long inicio = System.nanoTime();
        try {
            String resposta = embrulhado.fetchRaw(); // sempre delega
            long ms = (System.nanoTime() - inicio) / 1_000_000;
            saida.println(String.format("[LOG] %s respondeu %d bytes em %d ms", fonte, resposta.length(), ms));
            return resposta;
        } catch (SpaceApiException e) {
            long ms = (System.nanoTime() - inicio) / 1_000_000;
            saida.println(String.format("[LOG] %s falhou após %d ms: %s", fonte, ms, e.getMessage()));
            throw e;
        }
    }
}
