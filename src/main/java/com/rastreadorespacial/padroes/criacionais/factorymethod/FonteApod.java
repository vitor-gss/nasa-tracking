package com.rastreadorespacial.padroes.criacionais.factorymethod;

import com.rastreadorespacial.domain.ApodInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.rastreadorespacial.api.ApodClient;
import com.rastreadorespacial.api.SpaceDataClient;
import com.rastreadorespacial.padroes.criacionais.singleton.JsonMapper;
import com.rastreadorespacial.domain.SpaceObject;

import java.io.IOException;
import java.util.List;

/**
 * ConcreteCreator (Factory Method) e ConcreteClass (Template Method) para o feed NASA APOD.
 *
 * <p>O APOD não traz objetos rastreáveis: {@link #interpretar} devolve lista vazia e o trabalho específico
 * fica no gancho {@link #aposInterpretar}, que lê os metadados da imagem do dia.</p>
 */
public final class FonteApod extends FonteDeDados {

    private ApodInfo apod;

    public FonteApod() {
        super("apod");
    }

    public FonteApod(SpaceDataClient cliente) {
        super("apod", cliente);
    }

    @Override
    protected SpaceDataClient criarCliente() {
        return new ApodClient();
    }

    @Override
    protected List<? extends SpaceObject> interpretar(String rawJson) {
        return List.of();
    }

    @Override
    protected void aposInterpretar(String rawJson) {
        try {
            JsonNode root = JsonMapper.INSTANCE.mapper().readTree(rawJson);
            apod = new ApodInfo(root.path("title").asText(""), root.path("date").asText(""),
                    root.path("explanation").asText(""),
                    root.path("hdurl").asText(root.path("url").asText("")));
        } catch (IOException | RuntimeException e) {
            apod = null;
        }
    }

    @Override
    protected String mensagemDeSucesso(int quantidade) {
        return apod != null ? "APOD: metadados carregados." : "[AVISO] Não foi possível interpretar o APOD.";
    }

    /** @return os metadados da última carga, ou null se indisponíveis */
    public ApodInfo apod() {
        return apod;
    }
}
