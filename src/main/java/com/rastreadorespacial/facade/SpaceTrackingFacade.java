package com.rastreadorespacial.facade;

import com.rastreadorespacial.api.SpaceClientFactory;
import com.rastreadorespacial.api.SpaceDataClient;
import com.rastreadorespacial.api.SpaceDataParser;
import com.rastreadorespacial.api.SpaceApiException;
import com.rastreadorespacial.cache.CachedDataResult;
import com.rastreadorespacial.cache.SpaceDataService;
import com.rastreadorespacial.domain.Asteroid;
import com.rastreadorespacial.domain.Comet;
import com.rastreadorespacial.domain.DailySnapshot;
import com.rastreadorespacial.domain.Satellite;
import com.rastreadorespacial.domain.TrackedObjectRegistry;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Facade para simplificar o carregamento e a composição de dados espaciais
 * vindos de múltiplas fontes (NeoWs, APOD, Celestrak e SBDB).
 */
public final class SpaceTrackingFacade {

    private final SpaceDataService dataService;
    private final Map<String, SpaceDataClient> clients;
    private final TrackedObjectRegistry registry = new TrackedObjectRegistry();

    public SpaceTrackingFacade() {
        this(new SpaceDataService(), SpaceClientFactory.defaultClients());
    }

    public SpaceTrackingFacade(SpaceDataService dataService, Map<String, SpaceDataClient> clients) {
        this.dataService = Objects.requireNonNull(dataService, "O serviço de dados não pode ser nulo.");
        this.clients = Map.copyOf(Objects.requireNonNull(clients, "Os clientes não podem ser nulos."));
    }

    public int carregarTodos() {
        return carregarTodos(true);
    }

    public int carregarTodos(boolean forcarApi) {
        int antes = registry.quantidade();
        for (String source : List.of("neows", "apod", "celestrak", "sbdb")) {
            carregarFonte(source, forcarApi);
        }
        return registry.quantidade() - antes;
    }

    public boolean carregarFonte(String source) {
        return carregarFonte(source, true);
    }

    public boolean carregarFonte(String source, boolean forcarApi) {
        SpaceDataClient client = clients.get(source);
        if (client == null) {
            return false;
        }

        try {
            CachedDataResult result = dataService.fetch(source, client, forcarApi);
            switch (source) {
                case "neows" -> {
                    registry.adicionarTodos(SpaceDataParser.parseNeoWs(result.rawJson()));
                }
                case "celestrak" -> {
                    registry.adicionarTodos(SpaceDataParser.parseCelestrak(result.rawJson()));
                }
                case "sbdb" -> {
                    registry.adicionarTodos(SpaceDataParser.parseSbdb(result.rawJson()));
                }
                case "apod" -> {
                    // APOD é processado por outro componente; aqui apenas mantém a integração do facade.
                    return true;
                }
                default -> {
                    return false;
                }
            }
            return true;
        } catch (SpaceApiException e) {
            return false;
        }
    }

    public DailySnapshot snapshotAtual() {
        return new DailySnapshot(LocalDate.now(), registry.listarTodos());
    }

    public TrackedObjectRegistry getRegistry() {
        return registry;
    }
}
