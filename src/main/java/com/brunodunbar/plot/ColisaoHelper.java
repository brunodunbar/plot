package com.brunodunbar.plot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class ColisaoHelper {

    private static final Logger LOGGER = LogManager.getLogger(ColisaoHelper.class);

    public static class ColisaoResult {
        private boolean pontoColisao;
        private Coordenada coordenada;
        private String mensagem;

        public ColisaoResult(boolean pontoColisao, Coordenada coordenada, String mensagem) {
            this.pontoColisao = pontoColisao;
            this.coordenada = coordenada;
            this.mensagem = mensagem;
        }

        public boolean hasPontoColisao() {
            return pontoColisao;
        }

        public Coordenada getCoordenada() {
            return coordenada;
        }

        public String getMensagem() {
            return mensagem;
        }
    }

    public static ColisaoResult getColisao(Aviao aviao1, Aviao aviao2, double intervaloMinimo) {
        CoordenadaCartesiana coordenadaColisao;
        try {
            coordenadaColisao = getCoordenadaColisao(aviao1.getCoordenada(), aviao1.getDirecao(),
                    aviao2.getCoordenada(), aviao2.getDirecao());
        } catch (MesmaDirecaoException e) {
            return new ColisaoResult(false, CoordenadaCartesiana.INVALID, "sem colisão pois estão voando ma mesma direção");
        } catch (DirecaoOpostasException e) {
            return new ColisaoResult(true, CoordenadaCartesiana.INVALID, "estão voando em direção opostas");
        }

        double angle1 = aviao1.getCoordenada().asCartesiana().getAngle(coordenadaColisao);
        double angle2 = aviao2.getCoordenada().asCartesiana().getAngle(coordenadaColisao);

        LOGGER.debug("Angulo " + aviao1.getCoordenada() + " para " + coordenadaColisao + " " + angle1);
        LOGGER.debug("Angulo " + aviao2.getCoordenada() + " para " + coordenadaColisao + " " + angle2);

        if(angle1 > 0 && angle1 != aviao1.getDirecao()) {
            return new ColisaoResult(true, coordenadaColisao, "o avião " + aviao1.getDescricao() + " está voando para longe do ponto de colisão");
        }

        if(angle2 > 0 && angle2 != aviao2.getDirecao()) {
            return new ColisaoResult(true, coordenadaColisao, "o avião " + aviao2.getDescricao() + " está voando para longe do ponto de colisão");
        }

        BigDecimal tempo1 = ColisaoHelper.getTempoColisao(coordenadaColisao.distance(aviao1.getCoordenada()), aviao1.getVelocidade());
        BigDecimal tempo2 = ColisaoHelper.getTempoColisao(coordenadaColisao.distance(aviao2.getCoordenada()), aviao2.getVelocidade());

        LOGGER.debug("tempo de " + aviao1.getCoordenada() + " para " + coordenadaColisao + " " + tempo1);
        LOGGER.debug("tempo de " + aviao2.getCoordenada() + " para " + coordenadaColisao + " " + tempo2);

        BigDecimal intervalo = tempo1.subtract(tempo2).abs();

        if(intervalo.doubleValue() >= intervaloMinimo) {
            return new ColisaoResult(true, coordenadaColisao, "sem colisão, respeitando intervalo de segurança (" + intervalo + ")");
        }

        LOGGER.debug("intervalo " + intervalo);

        return new ColisaoResult(true, coordenadaColisao, "colisão, abaixo do intervalo de segurança (" + intervalo + ")");
    }

    public static CoordenadaCartesiana getCoordenadaColisao(Coordenada coordenada1, double direcao1, Coordenada coordenada2, double direcao2)
            throws MesmaDirecaoException, DirecaoOpostasException {

        LOGGER.debug("-----------------------------------------------");
        LOGGER.debug("Colisão de " + coordenada1 + " " + direcao1 + ", " + coordenada2 + " " + direcao2);

        // mesma direção
        if (direcao1 == direcao2) {
            throw new MesmaDirecaoException();
        }

//        if (Math.abs(direcao1 - direcao2) >= 180) {
//            return CoordenadaCartesiana.INVALID;
//        }

        CoordenadaCartesiana cartesiana1 = coordenada1.asCartesiana();
        BigDecimal angular = cartesiana1.getCoeficienteAngular(direcao1);
        BigDecimal linear = cartesiana1.getCoeficienteLinear(angular);

        LOGGER.debug(coordenada1 + " angular: " + angular);
        LOGGER.debug(coordenada1 + " lienar: " + linear);

        CoordenadaCartesiana cartesiana2 = coordenada2.asCartesiana();
        BigDecimal outroAngular = cartesiana2.getCoeficienteAngular(direcao2);
        BigDecimal outroLinear = cartesiana2.getCoeficienteLinear(outroAngular);

        LOGGER.debug(coordenada2 + " angular: " + outroAngular);
        LOGGER.debug(coordenada2 + " lienar: " + outroLinear);

        BigDecimal divisor = angular.add(outroAngular.negate());

        LOGGER.debug("divisor: " + divisor);

        if (BigDecimal.ZERO.compareTo(divisor) == 0) {
            throw new DirecaoOpostasException();
        }

        BigDecimal x = outroLinear.add(linear.negate()).divide(divisor, BigDecimal.ROUND_HALF_UP);
        BigDecimal y = angular.multiply(x).add(linear);

        CoordenadaCartesiana coordenadaCartesiana = CoordenadaCartesiana.of(x, y);

        LOGGER.debug("colisão " + coordenadaCartesiana);

        return coordenadaCartesiana;
    }

    public static BigDecimal getTempoColisao(BigDecimal distancia, double velocidade) {
        return distancia.divide(BigDecimal.valueOf(velocidade), BigDecimal.ROUND_HALF_UP).setScale(Constants.PRESISION, BigDecimal.ROUND_HALF_UP);
    }
}
