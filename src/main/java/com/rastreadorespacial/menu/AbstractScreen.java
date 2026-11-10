package com.rastreadorespacial.menu;

import com.rastreadorespacial.padroes.estruturais.facade.MenuContext;

import com.rastreadorespacial.domain.DistanceUnit;

import java.util.Objects;

abstract class AbstractScreen implements Screen {
    protected final MenuNavigator navigator;
    protected final MenuContext context;

    protected AbstractScreen(MenuNavigator navigator, MenuContext context) {
        this.navigator = Objects.requireNonNull(navigator);
        this.context = Objects.requireNonNull(context);
    }

    protected void cabecalho() {
        navigator.exibirBreadcrumb();
    }

    protected Integer numero(String entrada, int maximo) {
        try {
            int valor = Integer.parseInt(entrada);
            if (valor < 0 || valor > maximo) {
                throw new NumberFormatException();
            }
            return valor;
        } catch (NumberFormatException e) {
            navigator.output().println("Opção inválida. Digite um número entre 0 e " + maximo + ".");
            return null;
        }
    }

    /** Pergunta ao usuário a unidade de distância e a repassa para a fachada (MenuContext). */
    protected void escolherUnidadeDistancia() {
        navigator.output().println("Escolha a unidade de distância:");
        navigator.output().println("1 - Quilômetros");
        navigator.output().println("2 - Milhas");
        navigator.output().println("3 - Distâncias lunares");
        navigator.output().println("Digite sua opção:");
        String entrada = navigator.scanner().hasNextLine() ? navigator.scanner().nextLine().trim() : "";
        try {
            context.definirUnidadeDistancia(DistanceUnit.fromChoice(Integer.parseInt(entrada)));
            navigator.output().println("Unidade de distância atualizada para: " + context.unidadeDistancia().name());
        } catch (NumberFormatException e) {
            navigator.output().println("Opção inválida. Mantendo a unidade atual: " + context.unidadeDistancia().name());
        }
    }

    protected void voltarOuAvisar(int escolha) {
        if (escolha == 0) {
            navigator.voltar();
        }
    }
}
