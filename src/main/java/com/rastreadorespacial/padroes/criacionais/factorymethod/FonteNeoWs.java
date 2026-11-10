package com.rastreadorespacial.padroes.criacionais.factorymethod;

import com.rastreadorespacial.api.NeoWsClient;
import com.rastreadorespacial.api.SpaceDataClient;
import com.rastreadorespacial.api.SpaceDataParser;
import com.rastreadorespacial.domain.SpaceObject;

import java.util.List;

/** ConcreteCreator (Factory Method) e ConcreteClass (Template Method) para o feed NASA NeoWs. */
public final class FonteNeoWs extends FonteDeDados {

    public FonteNeoWs() {
        super("neows");
    }

    public FonteNeoWs(SpaceDataClient cliente) {
        super("neows", cliente);
    }

    @Override
    protected SpaceDataClient criarCliente() {
        return new NeoWsClient();
    }

    @Override
    protected List<? extends SpaceObject> interpretar(String rawJson) {
        return SpaceDataParser.parseNeoWs(rawJson);
    }

    @Override
    protected String mensagemDeSucesso(int quantidade) {
        return "NeoWs: " + quantidade + " asteroide(s) carregado(s).";
    }
}
