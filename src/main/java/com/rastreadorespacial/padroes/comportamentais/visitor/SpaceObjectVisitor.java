package com.rastreadorespacial.padroes.comportamentais.visitor;

import com.rastreadorespacial.domain.Asteroid;
import com.rastreadorespacial.domain.Comet;
import com.rastreadorespacial.domain.Satellite;
import com.rastreadorespacial.domain.SpaceObject;

/**
 * <strong>Padrão comportamental: Visitor — papel Visitor.</strong>
 *
 * <p>Representa uma operação sobre os objetos espaciais com um método {@code visitar} para cada tipo
 * concreto. Antes, cada operação (descrever, formatar para o painel, nomear o tipo) repetia uma cadeia
 * {@code if (objeto instanceof Asteroid) ... else if (objeto instanceof Comet) ...}. Agora cada operação
 * é uma classe que implementa esta interface, e os elementos escolhem o método certo por
 * <em>despacho duplo</em>: {@code objeto.aceitar(visitante)} &rarr; {@code visitante.visitar(this)}.</p>
 *
 * <ul>
 *   <li><b>Element:</b> {@link SpaceObject#aceitar(SpaceObjectVisitor)}</li>
 *   <li><b>ConcreteElement:</b> {@link Asteroid}, {@link Comet}, {@link Satellite}</li>
 *   <li><b>ConcreteVisitor:</b> {@code DescricaoObjetoVisitor}, {@link TipoObjetoVisitor}, o visitante do painel</li>
 * </ul>
 *
 * @param <R> tipo do resultado da operação
 */
public interface SpaceObjectVisitor<R> {

    R visitar(Asteroid asteroide);

    R visitar(Comet cometa);

    R visitar(Satellite satelite);

    /**
     * Usado por objetos espaciais que não são de nenhum dos tipos acima (por exemplo, dublês de teste).
     *
     * @param objeto objeto de outro tipo
     * @return resultado padrão da operação
     */
    R visitarOutro(SpaceObject objeto);
}
