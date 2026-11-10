/**
 * <h1>Padrões criacionais</h1>
 * <ul>
 *   <li><b>Singleton</b> (singleton/JsonMapper). EM USO: SbdbClient, SpaceDataParser, FonteApod,
 *       SnapshotHistoryStore, Dashboard.</li>
 *   <li><b>Factory Method</b> (factorymethod/FonteDeDados.criarCliente() e as subclasses FonteNeoWs, FonteApod,
 *       FonteCelestrak, FonteSbdb). EM USO: FonteDeDados.cliente(), chamado pelo MenuContext em toda carga de feed.</li>
 *   <li><b>Builder</b> (fica em domain/Satellite.Builder, classe interna do Satellite). EM USO:
 *       SpaceDataParser.parseCelestrak().</li>
 * </ul>
 */
package com.rastreadorespacial.padroes.criacionais;
