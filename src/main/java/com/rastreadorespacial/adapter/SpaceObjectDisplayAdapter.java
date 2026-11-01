package com.rastreadorespacial.adapter;

import com.rastreadorespacial.domain.Asteroid;
import com.rastreadorespacial.domain.Comet;
import com.rastreadorespacial.domain.Distance;
import com.rastreadorespacial.domain.DistanceUnit;
import com.rastreadorespacial.domain.HasDistance;
import com.rastreadorespacial.domain.Position;
import com.rastreadorespacial.domain.Satellite;
import com.rastreadorespacial.domain.SpaceObject;

import java.util.Locale;

/**
 * Adapter responsável por adaptar qualquer SpaceObject para a representação de exibição do menu.
 *
 * <p>Ele converte diferentes tipos concretos para uma interface comum de saída textual,
 * sem espalhar condicionais por toda a UI.</p>
 */
public final class SpaceObjectDisplayAdapter {

    private final SpaceObject objeto;
    private final DistanceUnit unidade;

    public SpaceObjectDisplayAdapter(SpaceObject objeto, DistanceUnit unidade) {
        this.objeto = objeto;
        this.unidade = unidade;
    }

    public String formatar() {
        if (objeto instanceof Asteroid asteroid) {
            return String.format(Locale.US, "Asteroide %s — %s",
                    asteroid.getNome(), formatarDistancia(asteroid.getDistanciaDePassagem(), unidade));
        }
        if (objeto instanceof Comet comet) {
            String nucleo = comet.getDiametroNucleoKm() == null ? "N/D"
                    : String.format(Locale.US, "%.1f", comet.getDiametroNucleoKm());
            return String.format("Cometa %s — periélio %s — núcleo %s km",
                    comet.getNome(), formatarDistancia(comet.getDistanciaPerielio(), unidade), nucleo);
        }
        if (objeto instanceof Satellite satellite) {
            String decaimento = satellite.getDecaimentoOrbitalDiario() == null ? "N/D"
                    : String.format(Locale.US, "%.3f", satellite.getDecaimentoOrbitalDiario());
            return String.format(Locale.US, "Satélite %s (NORAD %s) — decaimento %s km/dia",
                    satellite.getNome(), satellite.getId(), decaimento);
        }
        return objeto.getNome();
    }

    public String formatarDistancia(Distance distancia) {
        return formatarDistancia(distancia, unidade);
    }

    public static String formatarDistancia(Distance distancia, DistanceUnit unidade) {
        return switch (unidade) {
            case KILOMETERS -> String.format(Locale.US, "%.3f km", distancia.emQuilometros());
            case MILES -> String.format(Locale.US, "%.3f mi", distancia.emMilhas());
            case LUNAR_DISTANCES -> String.format(Locale.US, "%.3f LD", distancia.emDistanciasLunares());
        };
    }

    public String formatarPosicao(Position position) {
        return String.format(Locale.US, "%s — lat %.2f°, lon %.2f°, altitude %s",
                objeto.getNome(), position.latitudeGraus(), position.longitudeGraus(),
                formatarDistancia(position.altitude()));
    }

    public String formatarDistanciaDeReferencia() {
        if (objeto instanceof HasDistance hasDistance) {
            return hasDistance.getDistanciaDeReferencia()
                    .map(distance -> String.format(Locale.US, "%s — %s",
                            objeto.getNome(), formatarDistancia(distance)))
                    .orElse(objeto.getNome() + " — distância não aplicável");
        }
        return objeto.getNome() + " — distância não disponível";
    }
}
