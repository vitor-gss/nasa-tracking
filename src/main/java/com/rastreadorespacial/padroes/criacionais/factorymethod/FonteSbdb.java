package com.rastreadorespacial.padroes.criacionais.factorymethod;

import com.rastreadorespacial.api.SbdbClient;
import com.rastreadorespacial.api.SpaceDataClient;
import com.rastreadorespacial.api.SpaceDataParser;
import com.rastreadorespacial.domain.SpaceObject;

import java.util.List;

/** ConcreteCreator (Factory Method) e ConcreteClass (Template Method) para o feed NASA/JPL SBDB (cometas). */
public final class FonteSbdb extends FonteDeDados {

    public FonteSbdb() {
        super("sbdb");
    }

    public FonteSbdb(SpaceDataClient cliente) {
        super("sbdb", cliente);
    }

    @Override
    protected SpaceDataClient criarCliente() {
        return new SbdbClient();
    }

    @Override
    protected List<? extends SpaceObject> interpretar(String rawJson) {
        return SpaceDataParser.parseSbdb(rawJson);
    }

    @Override
    protected String mensagemDeSucesso(int quantidade) {
        return "SBDB: " + quantidade + " cometa(s) carregado(s).";
    }
}
