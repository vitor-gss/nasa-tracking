/**
 * <b>Factory Method</b> (criacional) <b>+ Template Method</b> (comportamental) na mesma hierarquia.
 *
 * <ul>
 *   <li>Creator / AbstractClass: FonteDeDados</li>
 *   <li>Factory Method: criarCliente() &mdash; cada subclasse cria o seu cliente HTTP.</li>
 *   <li>Template Method: carregar() (final) &mdash; buscar, interpretar, registrar, gancho, mensagem.</li>
 *   <li>ConcreteCreators / ConcreteClasses: FonteNeoWs, FonteApod, FonteCelestrak, FonteSbdb.</li>
 * </ul>
 *
 * <p><b>Status: EM USO.</b> O MenuContext cria as quatro fontes e chama carregar() em atualizar(); na primeira
 * carga, cliente() aciona o Factory Method.</p>
 */
package com.rastreadorespacial.padroes.criacionais.factorymethod;
