/**
 * <b>Visitor.</b> Visitor = SpaceObjectVisitor; ConcreteVisitors = DescricaoObjetoVisitor, TipoObjetoVisitor e
 * Dashboard.DescricaoPainelVisitor; Elements = Asteroid, Comet, Satellite (método aceitar).
 *
 * <p><b>Status: EM USO.</b> DescricaoObjetoVisitor em MenuFormatters.objeto() (todas as listagens do menu);
 * TipoObjetoVisitor em RastrearObjetosScreen ("Tipo: Asteroide/Cometa/Satélite"). O visitante do Dashboard só é
 * exercitado pelos testes, porque o Dashboard não é chamado pelo Main.</p>
 */
package com.rastreadorespacial.padroes.comportamentais.visitor;
