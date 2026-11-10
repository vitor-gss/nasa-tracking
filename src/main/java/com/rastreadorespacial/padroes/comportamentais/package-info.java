/**
 * <h1>Padrões comportamentais</h1>
 * <ul>
 *   <li><b>Observer</b> (observer/*). EM USO: MenuContext (sujeito) notifica ConsoleFeedObserver.</li>
 *   <li><b>Visitor</b> (visitor/*). EM USO: MenuFormatters.objeto(), RastrearObjetosScreen, Dashboard;
 *       elementos: Asteroid, Comet, Satellite (método aceitar).</li>
 *   <li><b>Template Method</b> (fica em criacionais/factorymethod/FonteDeDados.carregar(), porque a mesma classe
 *       também é o Creator do Factory Method). EM USO: MenuContext.atualizar().</li>
 *   <li><b>State</b> (fica no pacote menu): Context = MenuNavigator, State = Screen, ConcreteState = cada tela.
 *       EM USO: Main chama MenuNavigator.loop(), que delega para a tela atual.</li>
 * </ul>
 */
package com.rastreadorespacial.padroes.comportamentais;
