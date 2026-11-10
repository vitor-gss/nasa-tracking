package com.rastreadorespacial.menu;

import com.rastreadorespacial.padroes.comportamentais.visitor.DescricaoObjetoVisitor;

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
        // Visitor: o próprio objeto escolhe o método de descrição certo (sem instanceof)
        return objeto.aceitar(new DescricaoObjetoVisitor(unidade));
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
        if (objeto instanceof HasDistance hasDistance) {
            return hasDistance.getDistanciaDeReferencia()
                    .map(distance -> String.format(Locale.US, "%s — %s",
                            objeto.getNome(), formatarDistancia(distance, unidade)))
                    .orElse(objeto.getNome() + " — distância não aplicável");
        }
        return objeto.getNome() + " — distância não disponível";
    }

    static String posicao(SpaceObject objeto, Position position) {
        return posicao(objeto, position, DistanceUnit.KILOMETERS);
    }

    static String posicao(SpaceObject objeto, Position position, DistanceUnit unidade) {
        return String.format(Locale.US, "%s — lat %.2f°, lon %.2f°, altitude %s",
                objeto.getNome(), position.latitudeGraus(), position.longitudeGraus(),
                formatarDistancia(position.altitude(), unidade));
    }

    private static String formatarDistancia(Distance distancia, DistanceUnit unidade) {
        return DescricaoObjetoVisitor.formatarDistancia(distancia, unidade);
    }
}
