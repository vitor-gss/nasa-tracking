package com.rastreadorespacial.padroes.estruturais.facade;

import com.rastreadorespacial.api.SpaceApiException;
import com.rastreadorespacial.api.SpaceDataClient;
import com.rastreadorespacial.cache.CachedDataResult;
import com.rastreadorespacial.cache.SpaceDataService;
import com.rastreadorespacial.domain.DailySnapshot;
import com.rastreadorespacial.domain.DistanceUnit;
import com.rastreadorespacial.domain.TrackedObjectRegistry;
import com.rastreadorespacial.domain.ApodInfo;
import com.rastreadorespacial.padroes.comportamentais.observer.ConsoleFeedObserver;
import com.rastreadorespacial.padroes.comportamentais.observer.EventoFeed;
import com.rastreadorespacial.padroes.comportamentais.observer.FeedObserver;
import com.rastreadorespacial.padroes.criacionais.factorymethod.FonteApod;
import com.rastreadorespacial.padroes.criacionais.factorymethod.FonteCelestrak;
import com.rastreadorespacial.padroes.criacionais.factorymethod.FonteDeDados;
import com.rastreadorespacial.padroes.criacionais.factorymethod.FonteNeoWs;
import com.rastreadorespacial.padroes.criacionais.factorymethod.FonteSbdb;
import com.rastreadorespacial.padroes.criacionais.factorymethod.ResultadoCarga;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Ponto de entrada das telas para o restante do sistema. Cumpre dois papéis de padrões:
 *
 * <h2>Padrão estrutural: Facade</h2>
 * <p>As telas não conhecem {@link SpaceDataService}, os clientes HTTP, o parser, o cache nem as fontes.
 * Elas chamam poucas operações de alto nível — {@link #atualizarTodos()}, {@link #atualizar(String)},
 * {@link #snapshot()}, {@link #registry()}, {@link #apod()} — e a fachada coordena o subsistema
 * (fontes &rarr; serviço com retry/cache &rarr; parser &rarr; registro). A fachada não lê teclado nem
 * escreve no console: apenas coordena e delega.</p>
 *
 * <h2>Padrão comportamental: Observer — papel Subject</h2>
 * <p>Mantém uma lista de {@link FeedObserver}. Ao terminar cada carga, publica um {@link EventoFeed}
 * para todos os inscritos ({@link #inscrever}/{@link #cancelar}), sem saber quem são.</p>
 */
public final class MenuContext {

    private final SpaceDataService dataService;
    private final Map<String, FonteDeDados> fontes = new LinkedHashMap<>();
    private final FonteApod fonteApod;
    private final Map<String, CachedDataResult> ultimoCarregamento = new LinkedHashMap<>();
    private final TrackedObjectRegistry registry = new TrackedObjectRegistry();
    private final List<FeedObserver> observadores = new ArrayList<>();
    private DistanceUnit unidadeDistancia = DistanceUnit.KILOMETERS;

    public MenuContext() {
        this(new SpaceDataService(), new FonteNeoWs(), new FonteApod(), new FonteCelestrak(), new FonteSbdb(), System.out);
    }

    public MenuContext(SpaceDataService dataService,
                       SpaceDataClient neoWsClient,
                       SpaceDataClient apodClient,
                       SpaceDataClient celestrakClient,
                       PrintStream output) {
        this(dataService, neoWsClient, apodClient, celestrakClient, () -> "[]", output);
    }

    public MenuContext(SpaceDataService dataService,
                       SpaceDataClient neoWsClient,
                       SpaceDataClient apodClient,
                       SpaceDataClient celestrakClient,
                       SpaceDataClient sbdbClient,
                       PrintStream output) {
        this(dataService, new FonteNeoWs(neoWsClient), new FonteApod(apodClient),
                new FonteCelestrak(celestrakClient), new FonteSbdb(sbdbClient), output);
    }

    private MenuContext(SpaceDataService dataService, FonteNeoWs neoWs, FonteApod apod,
                        FonteCelestrak celestrak, FonteSbdb sbdb, PrintStream output) {
        this.dataService = Objects.requireNonNull(dataService);
        this.fonteApod = Objects.requireNonNull(apod);
        for (FonteDeDados fonte : List.of(neoWs, apod, celestrak, sbdb)) {
            fontes.put(fonte.nome(), fonte);
        }
        // Inscrito padrão: imprime no terminal o resultado de cada carga.
        inscrever(new ConsoleFeedObserver(Objects.requireNonNull(output)));

        // Carrega os feeds iniciais (cache primeiro) para que o programa não abra com o registro vazio.
        atualizarTodos(false);
    }

    // ---------------------------------------------------------------- Observer (Subject)

    public void inscrever(FeedObserver observador) {
        observadores.add(Objects.requireNonNull(observador));
    }

    public void cancelar(FeedObserver observador) {
        observadores.remove(observador);
    }

    private void notificar(EventoFeed evento) {
        for (FeedObserver observador : List.copyOf(observadores)) {
            observador.aoAtualizarFeed(evento);
        }
    }

    // ---------------------------------------------------------------- Facade

    public int atualizarTodos() {
        return atualizarTodos(true);
    }

    private int atualizarTodos(boolean forcarApi) {
        int antes = registry.quantidade();
        for (String source : fontes.keySet()) {
            atualizar(source, forcarApi);
        }
        return registry.quantidade() - antes;
    }

    public boolean atualizar(String source) {
        return atualizar(source, true);
    }

    private boolean atualizar(String source, boolean forcarApi) {
        FonteDeDados fonte = fontes.get(source);
        if (fonte == null) {
            return false;
        }
        try {
            ResultadoCarga resultado = fonte.carregar(dataService, registry, forcarApi); // Template Method
            ultimoCarregamento.put(source, resultado.dados());
            notificar(new EventoFeed(source, true, resultado.mensagem(), resultado.quantidade(),
                    resultado.dados().fromCache()));
            return true;
        } catch (SpaceApiException e) {
            notificar(new EventoFeed(source, false, "[ERRO] " + source + " indisponível: " + e.getMessage(), 0, false));
            return false;
        }
    }

    public TrackedObjectRegistry registry() {
        return registry;
    }

    public DailySnapshot snapshot() {
        return new DailySnapshot(LocalDate.now(), registry.listarTodos());
    }

    public ApodInfo apod() {
        return fonteApod.apod();
    }

    public Map<String, CachedDataResult> ultimoCarregamento() {
        return Map.copyOf(ultimoCarregamento);
    }

    public DistanceUnit unidadeDistancia() {
        return unidadeDistancia;
    }

    public void definirUnidadeDistancia(DistanceUnit unidade) {
        this.unidadeDistancia = Objects.requireNonNull(unidade);
    }
}
