/**
 * <h1>Padrões de projeto (GoF) aplicados no Rastreador Espacial</h1>
 *
 * <p>Cada subpasta contém as classes que <em>são</em> o padrão. Padrões que fazem parte de classes que não
 * podem sair do lugar sem quebrar a arquitetura ficam onde estão e são indicados aqui.</p>
 *
 * <table border="1">
 *   <caption>Índice dos padrões</caption>
 *   <tr><th>Categoria</th><th>Padrão</th><th>Onde está</th><th>Onde é USADO</th></tr>
 *   <tr><td rowspan="3">Criacional</td>
 *       <td>Singleton</td><td>padroes/criacionais/singleton/JsonMapper</td>
 *       <td>SbdbClient, SpaceDataParser, FonteApod, SnapshotHistoryStore, Dashboard</td></tr>
 *   <tr><td>Factory Method</td><td>padroes/criacionais/factorymethod/FonteDeDados.criarCliente()</td>
 *       <td>FonteDeDados.cliente(), chamado pelo MenuContext em toda carga de feed</td></tr>
 *   <tr><td>Builder</td><td>domain/Satellite.Builder (classe interna, fica no lugar)</td>
 *       <td>SpaceDataParser.parseCelestrak()</td></tr>
 *   <tr><td rowspan="3">Estrutural</td>
 *       <td>Proxy</td><td>padroes/estruturais/proxy/CacheEmMemoriaClient</td>
 *       <td>FonteDeDados.cliente() embrulha todo cliente real da NASA</td></tr>
 *   <tr><td>Decorator</td><td>padroes/estruturais/decorator/LogSpaceDataClient</td>
 *       <td>FonteDeDados.cliente() embrulha o Proxy (gera as linhas "[LOG] ...")</td></tr>
 *   <tr><td>Facade</td><td>padroes/estruturais/facade/MenuContext</td>
 *       <td>Main e todas as telas do menu</td></tr>
 *   <tr><td rowspan="4">Comportamental</td>
 *       <td>Observer</td><td>padroes/comportamentais/observer/*</td>
 *       <td>MenuContext (sujeito) publica para ConsoleFeedObserver a cada carga de feed</td></tr>
 *   <tr><td>Visitor</td><td>padroes/comportamentais/visitor/*</td>
 *       <td>MenuFormatters.objeto() (todas as listagens), RastrearObjetosScreen (Tipo:), Dashboard</td></tr>
 *   <tr><td>Template Method</td><td>padroes/criacionais/factorymethod/FonteDeDados.carregar()</td>
 *       <td>MenuContext.atualizar(); fica junto do Factory Method porque é a mesma classe</td></tr>
 *   <tr><td>State</td><td>menu/MenuNavigator, menu/Screen e as telas (ficam no lugar)</td>
 *       <td>Main chama MenuNavigator.loop(), que delega para a tela atual</td></tr>
 * </table>
 */
package com.rastreadorespacial.padroes;
