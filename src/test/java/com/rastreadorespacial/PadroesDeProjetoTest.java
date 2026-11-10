package com.rastreadorespacial;

import com.rastreadorespacial.padroes.estruturais.proxy.CacheEmMemoriaClient;
import com.rastreadorespacial.padroes.estruturais.decorator.LogSpaceDataClient;
import com.rastreadorespacial.api.RetryPolicy;
import com.rastreadorespacial.api.SpaceDataClient;
import com.rastreadorespacial.cache.RawDataStore;
import com.rastreadorespacial.cache.SpaceDataService;
import com.rastreadorespacial.padroes.criacionais.singleton.JsonMapper;
import com.rastreadorespacial.domain.Asteroid;
import com.rastreadorespacial.domain.Comet;
import com.rastreadorespacial.domain.Distance;
import com.rastreadorespacial.domain.Satellite;
import com.rastreadorespacial.padroes.comportamentais.visitor.TipoObjetoVisitor;
import com.rastreadorespacial.padroes.comportamentais.observer.EventoFeed;
import com.rastreadorespacial.padroes.criacionais.factorymethod.FonteNeoWs;
import com.rastreadorespacial.padroes.criacionais.factorymethod.ResultadoCarga;
import com.rastreadorespacial.domain.TrackedObjectRegistry;
import com.rastreadorespacial.padroes.estruturais.facade.MenuContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/** Verifica os padrões de projeto adicionados ao sistema. */
class PadroesDeProjetoTest {

    @TempDir
    Path tempDir;

    private static final String NEOWS_JSON = """
            {"near_earth_objects":{"2026-11-10":[
              {"id":"3542519","name":"Asteroide A","is_potentially_hazardous_asteroid":false,
               "close_approach_data":[{"miss_distance":{"lunar":"12.0"}}]}]}}
            """;

    // ---------------------------------------------------------------- criacionais

    @Test
    void singletonDevolveSempreAMesmaInstancia() {
        assertSame(JsonMapper.INSTANCE.mapper(), JsonMapper.INSTANCE.mapper());
    }

    @Test
    void builderMontaSateliteComCamposNomeados() {
        Satellite padrao = new Satellite.Builder("25544", "ISS").build();
        Satellite comDecaimento = new Satellite.Builder("25544", "ISS").decaimentoOrbitalDiario(0.02).build();
        assertEquals("ISS", padrao.getNome());
        assertNull(padrao.getDecaimentoOrbitalDiario());
        assertEquals(0.02, comDecaimento.getDecaimentoOrbitalDiario());
    }

    @Test
    void factoryMethodETemplateMethodCarregamFonte() throws Exception {
        SpaceDataService servico = new SpaceDataService(
                new RawDataStore(tempDir), new RetryPolicy(Duration.ZERO, ignored -> {}), 1);
        TrackedObjectRegistry registro = new TrackedObjectRegistry();
        ResultadoCarga resultado = new FonteNeoWs(() -> NEOWS_JSON).carregar(servico, registro, true);
        assertEquals(1, resultado.quantidade());
        assertEquals(1, registro.quantidade());
        assertEquals("NeoWs: 1 asteroide(s) carregado(s).", resultado.mensagem());
    }

    // ---------------------------------------------------------------- estruturais

    @Test
    void proxyEvitaChamadasRepetidasDentroDaValidade() throws Exception {
        AtomicInteger chamadas = new AtomicInteger();
        SpaceDataClient real = () -> "resposta-" + chamadas.incrementAndGet();
        Clock relogio = Clock.fixed(Instant.parse("2026-11-10T12:00:00Z"), ZoneOffset.UTC);
        CacheEmMemoriaClient proxy = new CacheEmMemoriaClient(real, Duration.ofMinutes(1), relogio);

        for (int i = 0; i < 5; i++) {
            assertEquals("resposta-1", proxy.fetchRaw());
        }
        assertEquals(1, proxy.chamadasReais());
    }

    @Test
    void decoratorRepassaEAcrescentaLog() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        SpaceDataClient decorado = new LogSpaceDataClient("teste", () -> "abc", new PrintStream(bytes));
        assertEquals("abc", decorado.fetchRaw());
        assertTrue(bytes.toString().contains("[LOG] teste respondeu 3 bytes"));
    }

    @Test
    void facadeEObserverPublicamEventosDeCarga() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        SpaceDataService servico = new SpaceDataService(
                new RawDataStore(tempDir), new RetryPolicy(Duration.ZERO, ignored -> {}), 1);
        MenuContext fachada = new MenuContext(servico, () -> NEOWS_JSON, () -> "{}", () -> "[]", new PrintStream(bytes));
        List<EventoFeed> recebidos = new ArrayList<>();
        fachada.inscrever(recebidos::add);

        fachada.atualizar("neows");

        assertEquals(1, recebidos.size());
        assertEquals("neows", recebidos.get(0).fonte());
        assertTrue(bytes.toString().contains("NeoWs: 1 asteroide(s) carregado(s)."));
        assertEquals(1, fachada.registry().quantidade());
    }

    // ---------------------------------------------------------------- comportamentais

    @Test
    void visitorEscolheOMetodoPeloTipoSemInstanceof() {
        TipoObjetoVisitor tipo = new TipoObjetoVisitor();
        assertEquals("Asteroide", new Asteroid("1", "A", Distance.ofLunarDistances(2), false).aceitar(tipo));
        assertEquals("Cometa", new Comet("1P", "Halley", Distance.ofKilometers(8.8e7), 11.0).aceitar(tipo));
        assertEquals("Satélite", new Satellite.Builder("1", "ISS").build().aceitar(tipo));
    }
}
