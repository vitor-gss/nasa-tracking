package com.rastreadorespacial.padroes.comportamentais.visitor;

import com.rastreadorespacial.domain.Asteroid;
import com.rastreadorespacial.domain.Comet;
import com.rastreadorespacial.domain.Distance;
import com.rastreadorespacial.domain.DistanceUnit;
import com.rastreadorespacial.domain.Satellite;
import com.rastreadorespacial.domain.SpaceObject;
import com.rastreadorespacial.padroes.comportamentais.visitor.SpaceObjectVisitor;

import java.util.Locale;
import java.util.Objects;

/**
 * <strong>Padrão comportamental: Visitor — ConcreteVisitor.</strong>
 *
 * <p>Operação "descreva este objeto na unidade escolhida pelo usuário". Cada tipo tem o seu
 * {@code visitar}; antes isso era uma cadeia de {@code instanceof} dentro de {@code MenuFormatters}.</p>
 */
public final class DescricaoObjetoVisitor implements SpaceObjectVisitor<String> {

    private final DistanceUnit unidade;

    public DescricaoObjetoVisitor(DistanceUnit unidade) {
        this.unidade = Objects.requireNonNull(unidade, "A unidade não pode ser nula.");
    }

    @Override
    public String visitar(Asteroid asteroide) {
        return String.format(Locale.US, "Asteroide %s — %s",
                asteroide.getNome(), formatarDistancia(asteroide.getDistanciaDePassagem(), unidade));
    }

    @Override
    public String visitar(Comet cometa) {
        String nucleo = cometa.getDiametroNucleoKm() == null ? "N/D"
                : String.format(Locale.US, "%.1f", cometa.getDiametroNucleoKm());
        return String.format("Cometa %s — periélio %s — núcleo %s km",
                cometa.getNome(), formatarDistancia(cometa.getDistanciaPerielio(), unidade), nucleo);
    }

    @Override
    public String visitar(Satellite satelite) {
        String decaimento = satelite.getDecaimentoOrbitalDiario() == null ? "N/D"
                : String.format(Locale.US, "%.3f", satelite.getDecaimentoOrbitalDiario());
        return String.format(Locale.US, "Satélite %s (NORAD %s) — decaimento %s km/dia",
                satelite.getNome(), satelite.getId(), decaimento);
    }

    @Override
    public String visitarOutro(SpaceObject objeto) {
        return objeto.getNome();
    }

    /**
     * Formata uma distância na unidade pedida, sempre com o símbolo da unidade junto (RF1).
     *
     * @param distancia distância a formatar
     * @param unidade   unidade de exibição
     * @return texto como "384400.000 km", "238855.000 mi" ou "1.000 LD"
     */
    public static String formatarDistancia(Distance distancia, DistanceUnit unidade) {
        return switch (unidade) {
            case KILOMETERS -> String.format(Locale.US, "%.3f km", distancia.emQuilometros());
            case MILES -> String.format(Locale.US, "%.3f mi", distancia.emMilhas());
            case LUNAR_DISTANCES -> String.format(Locale.US, "%.3f LD", distancia.emDistanciasLunares());
        };
    }
}
