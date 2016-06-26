package com.brunodunbar.plot;

import java.math.BigDecimal;
import java.util.Objects;

public class ColisaoHelper {

    public static CoordenadaCartesiana getCoordenadaColisao(Coordenada coordenada1, double direcao1, Coordenada coordenada2, double direcao2) {

        if(Math.abs(direcao1 - direcao2) >= 180) {
            return CoordenadaCartesiana.INVALID;
        }

        BigDecimal angular = coordenada1.asCartesiana().getCoeficienteAngular(direcao1);
        BigDecimal linear = coordenada1.asCartesiana().getCoeficienteLinear(angular);

        BigDecimal outroAngular = coordenada2.asCartesiana().getCoeficienteAngular(direcao2);
        BigDecimal outroLinear = coordenada2.asCartesiana().getCoeficienteLinear(outroAngular);

        BigDecimal divisor = outroAngular.subtract(angular);

        if (BigDecimal.ZERO.compareTo(divisor) == 0) {
            return CoordenadaCartesiana.INVALID;
        }

        BigDecimal x = linear.subtract(outroLinear).divide(divisor, BigDecimal.ROUND_HALF_UP);
        BigDecimal y = angular.multiply(x).add(linear);

        return CoordenadaCartesiana.of(x, y);
    }

    public static BigDecimal getTempoColisao(BigDecimal distancia, double velocidade) {
        return distancia.divide(BigDecimal.valueOf(velocidade), BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
