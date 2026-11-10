package com.rastreadorespacial.domain;

import com.rastreadorespacial.padroes.comportamentais.visitor.SpaceObjectVisitor;

/**
 * Interface comum para todos os objetos espaciais rastreáveis pelo sistema.
 */
public interface SpaceObject {

    /**
     * Retorna o identificador do objeto espacial.
     *
     * @return identificador
     */
    String getId();

    /**
     * Retorna o identificador canônico e estável do objeto espacial no mundo real
     * (ex: neo_reference_id da NASA, NORAD Catalog Number, designação IAU).
     * <p>Este identificador independe da data do feed ou da consulta.</p>
     *
     * @return identificador canônico
     */
    String getIdCanonico();

    /**
     * Retorna o nome de designação do objeto espacial.
     *
     * @return nome do objeto
     */
    String getNome();

    /**
     * Avalia e retorna o nível de risco oferecido pelo objeto espacial.
     * <p>A lógica de cálculo e os limiares numéricos são estritamente encapsulados
     * dentro de cada implementação concreta de objeto espacial.</p>
     *
     * @return nível de risco como enum {@link RiskLevel}
     */
    RiskLevel avaliarRisco();

    /**
     * <strong>Visitor — papel Element.</strong> Oferece este objeto ao visitante. Os tipos concretos
     * ({@link Asteroid}, {@link Comet}, {@link Satellite}) sobrescrevem chamando {@code visitante.visitar(this)},
     * o que seleciona o método certo sem {@code instanceof}. Outros tipos caem em {@code visitarOutro}.
     *
     * @param visitante operação a executar sobre este objeto
     * @param <R>       tipo do resultado
     * @return resultado da operação
     */
    default <R> R aceitar(SpaceObjectVisitor<R> visitante) {
        return visitante.visitarOutro(this);
    }
}
