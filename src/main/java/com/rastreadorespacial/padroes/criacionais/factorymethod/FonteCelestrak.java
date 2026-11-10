package com.rastreadorespacial.padroes.criacionais.factorymethod;

import com.rastreadorespacial.api.CelestrakClient;
import com.rastreadorespacial.api.SpaceDataClient;
import com.rastreadorespacial.api.SpaceDataParser;
import com.rastreadorespacial.domain.SpaceObject;

import java.util.List;

/** ConcreteCreator (Factory Method) e ConcreteClass (Template Method) para o feed Celestrak (satélites). */
public final class FonteCelestrak extends FonteDeDados {

    public FonteCelestrak() {
        super("celestrak");
    }

    public FonteCelestrak(SpaceDataClient cliente) {
        super("celestrak", cliente);
    }

    @Override
    protected SpaceDataClient criarCliente() {
        return new CelestrakClient();
    }

    @Override
    protected List<? extends SpaceObject> interpretar(String rawJson) {
        return SpaceDataParser.parseCelestrak(rawJson);
    }

    @Override
    protected String mensagemDeSucesso(int quantidade) {
        return "Celestrak: " + quantidade + " satélite(s) carregado(s).";
    }
}
