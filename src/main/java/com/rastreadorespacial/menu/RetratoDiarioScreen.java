package com.rastreadorespacial.menu;

import com.rastreadorespacial.padroes.estruturais.facade.MenuContext;

import com.rastreadorespacial.config.AppConfig;
import com.rastreadorespacial.domain.DailySnapshot;
import com.rastreadorespacial.domain.RiskLevel;
import com.rastreadorespacial.domain.SpaceObject;
import com.rastreadorespacial.domain.SnapshotFormatter;
import com.rastreadorespacial.domain.ApodInfo;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public final class RetratoDiarioScreen extends AbstractScreen {
    private final SnapshotHistoryStore historyStore = new SnapshotHistoryStore(AppConfig.DATA_DIR.resolve("snapshots"));
    private final Consumer<URI> browserOpener;

    public RetratoDiarioScreen(MenuNavigator navigator, MenuContext context) {
        this(navigator, context, RetratoDiarioScreen::abrirNoNavegador);
    }

    RetratoDiarioScreen(MenuNavigator navigator, MenuContext context, Consumer<URI> browserOpener) {
        super(navigator, context);
        this.browserOpener = Objects.requireNonNull(browserOpener);
    }

    @Override
    public String getNomeExibicao() {
        return "Retrato diário";
    }

    @Override
    public void exibir() {
        cabecalho();
        navigator.output().println("1 - Ver resumo textual do dia");
        navigator.output().println("2 - Ver estatísticas detalhadas");
        navigator.output().println("3 - Ver histórico de retratos anteriores");
        navigator.output().println("4 - Ver foto e descrição do objeto do dia (APOD)");
        navigator.output().println("0 - Voltar");
        navigator.output().println("Digite sua escolha:");
    }

    @Override
    public void tratarEscolha(String entrada) {
        Integer escolha = numero(entrada, 4);
        if (escolha == null) return;
        switch (escolha) {
            case 0 -> navigator.voltar();
            case 1 -> {
                DailySnapshot snapshot = context.snapshot();
                historyStore.salvar(snapshot);
                navigator.output().println(SnapshotFormatter.resumoTextual(snapshot));
            }
            case 2 -> imprimirEstatisticas();
            case 3 -> imprimirHistorico();
            case 4 -> imprimirApod();
            default -> { }
        }
    }

    private void imprimirApod() {
        ApodInfo apod = context.apod();
        navigator.output().println("APOD - Foto astronômica do dia:");
        if (apod == null) {
            navigator.output().println("Dados do objeto do dia indisponíveis. Atualize o feed APOD primeiro.");
            return;
        }
        navigator.output().println("Título: " + apod.titulo());
        navigator.output().println("Data: " + apod.data());
        navigator.output().println("Descrição: " + apod.explicacao());
        try {
            LocalDate data = LocalDate.parse(apod.data());
            URI paginaApod = URI.create("https://apod.nasa.gov/apod/ap"
                    + data.format(DateTimeFormatter.ofPattern("yyMMdd")) + ".html");
            navigator.output().println("Abrindo a foto oficial do APOD no navegador: " + paginaApod);
            browserOpener.accept(paginaApod);
        } catch (DateTimeParseException e) {
            navigator.output().println("[ERRO] Data inválida para abrir a foto do APOD: " + apod.data());
        } catch (UncheckedIOException | UnsupportedOperationException | SecurityException e) {
            navigator.output().println("[AVISO] Não foi possível abrir o navegador: " + e.getMessage());
            navigator.output().println("Acesse a página oficial do APOD manualmente.");
        }
    }

    private static void abrirNoNavegador(URI pagina) {
        if (!Desktop.isDesktopSupported()) {
            throw new UnsupportedOperationException("Este ambiente não oferece suporte a navegador.");
        }
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            throw new UnsupportedOperationException("Este ambiente não oferece suporte à abertura de links.");
        }
        try {
            desktop.browse(pagina);
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao abrir a página do APOD.", e);
        }
    }

    private void imprimirEstatisticas() {
        DailySnapshot snapshot = context.snapshot();
        historyStore.salvar(snapshot);
        navigator.output().println("Objetos ÚNICOS rastreados: " + snapshot.quantidadeDeObjetos());
        Optional<SpaceObject> proximo = snapshot.maisProximo();
        navigator.output().println("Mais próximo: " + proximo.map(MenuFormatters::distancia).orElse("nenhum"));
        navigator.output().println("Mais perigoso: " + proximoPerigoso(snapshot));
        navigator.output().println("Contagem por nível:");
        Map<RiskLevel, Long> contagens = snapshot.contagemPorNivelDeRisco();
        for (RiskLevel level : RiskLevel.values()) {
            navigator.output().println(" - " + level.name() + ": " + contagens.get(level));
        }
    }

    private String proximoPerigoso(DailySnapshot snapshot) {
        return snapshot.maisPerigoso()
                .map(objeto -> objeto.getNome() + " (" + objeto.avaliarRisco().name() + ")")
                .orElse("nenhum");
    }

    private void imprimirHistorico() {
        var datas = historyStore.listarDatas();
        if (datas.isEmpty()) {
            navigator.output().println("Nenhum retrato diário salvo ainda.");
            return;
        }
        navigator.output().println("Retratos salvos:");
        datas.forEach(data -> navigator.output().println(" - " + data));
    }
}
