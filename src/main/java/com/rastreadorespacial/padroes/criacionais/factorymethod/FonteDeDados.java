package com.rastreadorespacial.padroes.criacionais.factorymethod;

import com.rastreadorespacial.padroes.estruturais.proxy.CacheEmMemoriaClient;
import com.rastreadorespacial.padroes.estruturais.decorator.LogSpaceDataClient;
import com.rastreadorespacial.api.SpaceApiException;
import com.rastreadorespacial.api.SpaceDataClient;
import com.rastreadorespacial.cache.CachedDataResult;
import com.rastreadorespacial.cache.SpaceDataService;
import com.rastreadorespacial.domain.SpaceObject;
import com.rastreadorespacial.domain.TrackedObjectRegistry;

import java.util.List;
import java.util.Objects;

/**
 * Uma fonte de dados da NASA (NeoWs, APOD, Celestrak, SBDB). Esta classe abstrata combina dois padrões:
 *
 * <h2>Padrão criacional: Factory Method</h2>
 * <p>{@link #criarCliente()} é o <em>método de fábrica</em>: a classe base não sabe qual cliente HTTP
 * instanciar; cada subclasse ({@link FonteNeoWs}, {@link FonteApod}, ...) decide o produto concreto.
 * Uma fonte nova entra como mais uma subclasse, sem alterar as existentes.</p>
 * <ul>
 *   <li><b>Creator:</b> {@code FonteDeDados}</li>
 *   <li><b>ConcreteCreator:</b> {@code FonteNeoWs}, {@code FonteApod}, {@code FonteCelestrak}, {@code FonteSbdb}</li>
 *   <li><b>Product:</b> {@link SpaceDataClient}</li>
 * </ul>
 *
 * <h2>Padrão comportamental: Template Method</h2>
 * <p>{@link #carregar} é o <em>método-template</em> ({@code final}): fixa a ordem dos passos
 * buscar &rarr; interpretar &rarr; registrar &rarr; gancho &rarr; descrever. Os passos fixos ficam aqui;
 * os que variam ({@link #interpretar}, {@link #mensagemDeSucesso}) são abstratos e {@link #aposInterpretar}
 * é um gancho opcional. Antes, essa sequência era um {@code if/else} por fonte copiado em duas classes.</p>
 */
public abstract class FonteDeDados {

    private final String nome;
    private SpaceDataClient cliente;

    /** Usa o Factory Method para criar o cliente real na primeira vez que for necessário. */
    protected FonteDeDados(String nome) {
        this.nome = Objects.requireNonNull(nome, "O nome da fonte não pode ser nulo.");
    }

    /** Usa um cliente já pronto (por exemplo, um cliente falso em testes). */
    protected FonteDeDados(String nome, SpaceDataClient clienteInjetado) {
        this(nome);
        this.cliente = Objects.requireNonNull(clienteInjetado, "O cliente injetado não pode ser nulo.");
    }

    public final String nome() {
        return nome;
    }

    // ---------------------------------------------------------------- Factory Method

    /**
     * <b>Factory Method.</b> Cada subclasse decide qual cliente HTTP concreto criar.
     *
     * @return o cliente real da API desta fonte
     */
    protected abstract SpaceDataClient criarCliente();

    /**
     * Retorna o cliente desta fonte. Se nenhum foi injetado, cria o real pelo Factory Method e o embrulha:
     * Decorator de log &rarr; Proxy de cache em memória &rarr; cliente real.
     *
     * @return cliente pronto para uso
     */
    public final SpaceDataClient cliente() {
        if (cliente == null) {
            cliente = new LogSpaceDataClient(nome, new CacheEmMemoriaClient(criarCliente()), System.out);
        }
        return cliente;
    }

    // ---------------------------------------------------------------- Template Method

    /**
     * <b>Template Method.</b> A ordem dos passos é sempre a mesma e nenhuma subclasse pode alterá-la.
     *
     * @param servico   serviço com retry e cache em disco
     * @param registro  registro de objetos rastreados (deduplicação)
     * @param forcarApi true para tentar a API antes do cache
     * @return resumo da carga
     * @throws SpaceApiException se a API falhar e não houver cache
     */
    public final ResultadoCarga carregar(SpaceDataService servico, TrackedObjectRegistry registro, boolean forcarApi)
            throws SpaceApiException {
        CachedDataResult bruto = servico.fetch(nome, cliente(), forcarApi);   // 1. passo fixo
        List<? extends SpaceObject> objetos = interpretar(bruto.rawJson());    // 2. passo variável
        registro.adicionarTodos(objetos);                                     // 3. passo fixo
        aposInterpretar(bruto.rawJson());                                     // 4. gancho opcional
        return new ResultadoCarga(nome, bruto, objetos.size(),               // 5. passo variável
                mensagemDeSucesso(objetos.size()));
    }

    /** Passo variável: converte o JSON bruto desta fonte em objetos de domínio. */
    protected abstract List<? extends SpaceObject> interpretar(String rawJson);

    /** Passo variável: mensagem legível sobre o que foi carregado. */
    protected abstract String mensagemDeSucesso(int quantidade);

    /** Gancho: por padrão não faz nada; subclasses podem sobrescrever. */
    protected void aposInterpretar(String rawJson) {
    }
}
