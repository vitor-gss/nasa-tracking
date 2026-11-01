package com.rastreadorespacial.menu;

import com.rastreadorespacial.adapter.SpaceObjectDisplayAdapter;
import com.rastreadorespacial.domain.Distance;
import com.rastreadorespacial.domain.DistanceUnit;
import com.rastreadorespacial.domain.HasDistance;
import com.rastreadorespacial.domain.Position;
import com.rastreadorespacial.domain.SpaceObject;

import java.util.Locale;

final class MenuFormatters {
    private MenuFormatters() {}

    static String objeto(SpaceObject objeto) {
        return objeto(objeto, DistanceUnit.KILOMETERS);
    }

    static String objeto(SpaceObject objeto, DistanceUnit unidade) {
        return new SpaceObjectDisplayAdapter(objeto, unidade).formatar();
    }

    static String risco(SpaceObject objeto) {
        return risco(objeto, DistanceUnit.KILOMETERS);
    }

    static String risco(SpaceObject objeto, DistanceUnit unidade) {
        return String.format("[%-7s] %s", objeto.avaliarRisco().name(), objeto(objeto, unidade));
    }

    static String distancia(SpaceObject objeto) {
        return distancia(objeto, DistanceUnit.KILOMETERS);
    }

    static String distancia(SpaceObject objeto, DistanceUnit unidade) {
        return new SpaceObjectDisplayAdapter(objeto, unidade).formatarDistanciaDeReferencia();
    }

    static String posicao(SpaceObject objeto, Position position) {
        return posicao(objeto, position, DistanceUnit.KILOMETERS);
    }

    static String posicao(SpaceObject objeto, Position position, DistanceUnit unidade) {
        return new SpaceObjectDisplayAdapter(objeto, unidade).formatarPosicao(position);
    }

    private static String formatarDistancia(Distance distancia, DistanceUnit unidade) {
        return SpaceObjectDisplayAdapter.formatarDistancia(distancia, unidade);
    }
}
