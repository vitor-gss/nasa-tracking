package com.rastreadorespacial.padroes.comportamentais.visitor;

import com.rastreadorespacial.domain.Asteroid;
import com.rastreadorespacial.domain.Comet;
import com.rastreadorespacial.domain.Satellite;
import com.rastreadorespacial.domain.SpaceObject;

/**
 * <strong>Padrão comportamental: Visitor — ConcreteVisitor.</strong>
 *
 * <p>Operação "qual é o nome legível do tipo deste objeto?". Substitui o uso de
 * {@code objeto.getClass().getSimpleName()}, que expunha nomes de classe em inglês na tela.</p>
 */
public final class TipoObjetoVisitor implements SpaceObjectVisitor<String> {

    @Override
    public String visitar(Asteroid asteroide) {
        return "Asteroide";
    }

    @Override
    public String visitar(Comet cometa) {
        return "Cometa";
    }

    @Override
    public String visitar(Satellite satelite) {
        return "Satélite";
    }

    @Override
    public String visitarOutro(SpaceObject objeto) {
        return "Objeto espacial";
    }
}
