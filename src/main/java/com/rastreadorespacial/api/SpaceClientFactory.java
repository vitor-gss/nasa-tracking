package com.rastreadorespacial.api;

import java.util.Map;

/**
 * Factory para criação centralizada dos clientes de dados espaciais.
 *
 * <p>O padrão Factory evita espalhar a lógica de instanciação em vários pontos do sistema
 * e permite trocar a implementação concreta por outra sem alterar a API do cliente.</p>
 */
public final class SpaceClientFactory {

    public enum ClientType {
        NEO_WS,
        APOD,
        CELESTRAK,
        SBDB
    }

    private SpaceClientFactory() {
        throw new AssertionError("Factory estática, não deve ser instanciada.");
    }

    // Padrão Factory: centraliza a instanciação dos clientes de dados.
    // Assim, o restante do sistema depende da interface SpaceDataClient e não de implementações concretas.
    public static SpaceDataClient create(ClientType type) {
        return switch (type) {
            case NEO_WS -> new NeoWsClient();
            case APOD -> new ApodClient();
            case CELESTRAK -> new CelestrakClient();
            case SBDB -> new SbdbClient();
        };
    }

    // Factory também expõe um mapa com os clientes padrão, mantendo a criação em um único lugar.
    public static Map<String, SpaceDataClient> defaultClients() {
        return Map.of(
                "neows", create(ClientType.NEO_WS),
                "apod", create(ClientType.APOD),
                "celestrak", create(ClientType.CELESTRAK),
                "sbdb", create(ClientType.SBDB)
        );
    }
}
