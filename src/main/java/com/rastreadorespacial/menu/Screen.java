package com.rastreadorespacial.menu;

/**
 * <strong>Padrão comportamental: State — papel State.</strong> (EM USO)
 *
 * <p>Cada tela do menu é um estado concreto do {@link MenuNavigator}. Ao tratar uma escolha, a tela decide
 * para qual estado ir ({@code navigator.irPara(new OutraTela(...))}) ou volta ({@code navigator.voltar()}).</p>
 */
public interface Screen {
    void exibir();

    void tratarEscolha(String entrada);

    String getNomeExibicao();
}
