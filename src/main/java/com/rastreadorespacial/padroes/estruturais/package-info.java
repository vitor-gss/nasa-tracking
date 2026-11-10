/**
 * <h1>Padrões estruturais</h1>
 * <ul>
 *   <li><b>Proxy</b> (proxy/CacheEmMemoriaClient). EM USO: FonteDeDados.cliente().</li>
 *   <li><b>Decorator</b> (decorator/LogSpaceDataClient). EM USO: FonteDeDados.cliente().</li>
 *   <li><b>Facade</b> (facade/MenuContext). EM USO: Main e todas as telas do menu.</li>
 * </ul>
 * <p>Cadeia montada para cada fonte: LogSpaceDataClient (Decorator) &rarr; CacheEmMemoriaClient (Proxy) &rarr;
 * NeoWsClient / ApodClient / CelestrakClient / SbdbClient (objeto real).</p>
 */
package com.rastreadorespacial.padroes.estruturais;
