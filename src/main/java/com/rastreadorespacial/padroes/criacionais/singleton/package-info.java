/**
 * <b>Singleton (enum singleton).</b> JsonMapper garante uma única instância de ObjectMapper na aplicação.
 *
 * <p><b>Status: EM USO.</b> Usado por SbdbClient, SpaceDataParser, FonteApod, SnapshotHistoryStore e Dashboard
 * via {@code JsonMapper.INSTANCE.mapper()}.</p>
 */
package com.rastreadorespacial.padroes.criacionais.singleton;
