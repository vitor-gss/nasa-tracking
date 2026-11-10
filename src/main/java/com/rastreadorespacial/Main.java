package com.rastreadorespacial;

import com.rastreadorespacial.config.AppConfig;
import com.rastreadorespacial.menu.MainMenuScreen;
import com.rastreadorespacial.padroes.estruturais.facade.MenuContext;
import com.rastreadorespacial.menu.MenuNavigator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AppConfig.initDirectories();
        // Facade (+ Subject do Observer): cria as fontes (Factory Method), embrulhadas por Decorator e Proxy
        MenuContext context = new MenuContext();
        // State: o navegador delega para a tela atual; cada tela escolhe a próxima
        MenuNavigator navigator = new MenuNavigator(new Scanner(System.in), System.out);
        navigator.iniciar(new MainMenuScreen(navigator, context));
        navigator.loop();
    }
}
