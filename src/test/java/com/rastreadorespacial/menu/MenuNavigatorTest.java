package com.rastreadorespacial.menu;

import com.rastreadorespacial.padroes.estruturais.facade.MenuContext;

import com.rastreadorespacial.api.RetryPolicy;
import com.rastreadorespacial.api.SpaceDataClient;
import com.rastreadorespacial.cache.RawDataStore;
import com.rastreadorespacial.cache.SpaceDataService;
import com.rastreadorespacial.domain.Asteroid;
import com.rastreadorespacial.domain.Distance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuNavigatorTest {
    @TempDir
    Path tempDir;

    @Test
    void empilhaDesempilhaEEncerra() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Screen inicio = new DummyScreen("Início");
        MenuNavigator navigator = new MenuNavigator(inicio, new Scanner(""), new PrintStream(bytes));
        Screen segunda = new DummyScreen("Segunda");

        navigator.irPara(segunda);
        assertEquals(2, navigator.profundidade());
        assertEquals(segunda, navigator.telaAtual());

        navigator.voltar();
        assertEquals(1, navigator.profundidade());
        assertEquals(inicio, navigator.telaAtual());

        navigator.encerrar();
        assertFalse(navigator.estaExecutando());
        assertEquals(0, navigator.profundidade());
    }

    @Test
    void menuPrincipalNavegaERejeitaEntradaInvalida() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        MenuContext context = context(bytes);
        MenuNavigator navigator = new MenuNavigator(null, new Scanner(""), new PrintStream(bytes));
        MainMenuScreen main = new MainMenuScreen(navigator, context);
        navigator.iniciar(main);

        main.tratarEscolha("abc");
        assertEquals(main, navigator.telaAtual());
        assertTrue(bytes.toString().contains("Opção inválida"));
        main.tratarEscolha("1");
        assertTrue(navigator.telaAtual() instanceof ObjetosProximosScreen);
    }

    @Test
    void zeroVoltaEmTodasAsTelasComSubmenu() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        MenuContext context = context(bytes);
        MenuNavigator navigator = new MenuNavigator(null, new Scanner(""), new PrintStream(bytes));
        MainMenuScreen main = new MainMenuScreen(navigator, context);
        navigator.iniciar(main);

        Screen[] telas = {
                new ObjetosProximosScreen(navigator, context),
                new FiltroTipoScreen(navigator, context),
                new RastrearObjetosScreen(navigator, context),
                new RegistrosDiariosScreen(navigator, context),
                new RetratoDiarioScreen(navigator, context),
                new ConsultarFeedsScreen(navigator, context),
                new AtualizarFeedScreen(navigator, context)
        };
        for (Screen tela : telas) {
            navigator.irPara(tela);
            tela.tratarEscolha("0");
            assertEquals(main, navigator.telaAtual(), tela.getNomeExibicao());
        }
    }

    @Test
    void objetosProximosSoImprimeObjetosAposEscolha() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        MenuContext context = context(bytes);
        context.registry().adicionar(new Asteroid("1", "Asteroide de teste", Distance.ofKilometers(100), false));
        bytes.reset();
        MenuNavigator navigator = new MenuNavigator(new DummyScreen("Início"), new Scanner(""), new PrintStream(bytes));
        ObjetosProximosScreen screen = new ObjetosProximosScreen(navigator, context);

        screen.exibir();
        assertFalse(bytes.toString().contains("Asteroide de teste"));

        screen.tratarEscolha("1");
        assertTrue(bytes.toString().contains("Asteroide de teste"));
    }

    @Test
    void rastrearObjetosSoImprimeObjetosAposEscolha() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        MenuContext context = context(bytes);
        context.registry().adicionar(new Asteroid("1", "Asteroide de teste", Distance.ofKilometers(100), false));
        bytes.reset();
        MenuNavigator navigator = new MenuNavigator(new DummyScreen("Início"), new Scanner(""), new PrintStream(bytes));
        RastrearObjetosScreen screen = new RastrearObjetosScreen(navigator, context);

        screen.exibir();
        assertFalse(bytes.toString().contains("Asteroide de teste"));

        screen.tratarEscolha("1");
        assertTrue(bytes.toString().contains("Asteroide de teste"));
    }

    @Test
    void fotoDoDiaAbrePaginaOficialMesmoQuandoApiRetornaLogo() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        SpaceDataClient emptyClient = () -> "{}";
        SpaceDataClient apodClient = () -> """
                {"title":"NASA Science","date":"2026-11-10","explanation":"Saturn Behind the Moon",
                 "url":"https://science.nasa.gov/wp-content/themes/nasa-child/assets/images/nasa-logo@2x.png",
                 "hdurl":"https://science.nasa.gov/wp-content/themes/nasa-child/assets/images/nasa-logo@2x.png"}
                """;
        SpaceDataService service = new SpaceDataService(
                new RawDataStore(tempDir), new RetryPolicy(Duration.ZERO, ignored -> {}), 1);
        MenuContext context = new MenuContext(
                service, emptyClient, apodClient, emptyClient, new PrintStream(bytes));
        MenuNavigator navigator = new MenuNavigator(null, new Scanner(""), new PrintStream(bytes));
        AtomicReference<URI> openedPage = new AtomicReference<>();
        RetratoDiarioScreen screen = new RetratoDiarioScreen(navigator, context, openedPage::set);

        screen.tratarEscolha("4");

        assertEquals(URI.create("https://apod.nasa.gov/apod/ap261110.html"), openedPage.get());
        assertTrue(bytes.toString().contains("Abrindo a foto oficial do APOD no navegador"));
    }

    private MenuContext context(ByteArrayOutputStream bytes) {
        SpaceDataClient client = () -> "{}";
        SpaceDataService service = new SpaceDataService(
                new RawDataStore(tempDir), new RetryPolicy(Duration.ZERO, ignored -> {}), 1);
        return new MenuContext(service, client, client, client, new PrintStream(bytes));
    }

    private record DummyScreen(String getNomeExibicao) implements Screen {
        @Override
        public void exibir() {}

        @Override
        public void tratarEscolha(String entrada) {}
    }
}
