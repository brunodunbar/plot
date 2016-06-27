package com.brunodunbar.plot;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ColisaoHelperTest {

    @Test(expected = DirecaoOpostasException.class)
    public void deveDirecaoOpostas() throws DirecaoOpostasException, MesmaDirecaoException {
        assertEquals(CoordenadaCartesiana.INVALID,  ColisaoHelper.getCoordenadaColisao(CoordenadaCartesiana.of(5, 5),
                45, CoordenadaCartesiana.of(5, 10), 225));
    }

    @Test(expected = MesmaDirecaoException.class)
    public void deveMesmaDirecao() throws DirecaoOpostasException, MesmaDirecaoException {
        assertEquals(CoordenadaCartesiana.INVALID, ColisaoHelper.getCoordenadaColisao(CoordenadaCartesiana.of(5, 5),
                90, CoordenadaCartesiana.of(5, 10), 90));
    }

    @Test
    public void deveRetornarACoordenadaColisao() throws DirecaoOpostasException, MesmaDirecaoException {
        CoordenadaCartesiana coordenadaColisao = ColisaoHelper.getCoordenadaColisao(CoordenadaCartesiana.of(5, 5),
                45, CoordenadaCartesiana.of(5, 10), 135);

        assertEquals(new BigDecimal("7.50"), coordenadaColisao.getX());
        assertEquals(new BigDecimal("7.50"), coordenadaColisao.getY());
    }

    @Test
    public void deveRetornarADistanciaColisao() throws DirecaoOpostasException, MesmaDirecaoException {
        CoordenadaCartesiana coordenada1 = CoordenadaCartesiana.of(5, 5);
        CoordenadaCartesiana coordenada2 = CoordenadaCartesiana.of(5, 10);
        CoordenadaCartesiana coordenadaColisao = ColisaoHelper.getCoordenadaColisao(coordenada1,
                45, coordenada2, 135);

        assertEquals(new BigDecimal("3.54"), coordenadaColisao.distance(coordenada1));
        assertEquals(new BigDecimal("3.54"), coordenadaColisao.distance(coordenada2));
    }

    @Test
    public void deveRetornarOTempoColisao() {
        assertEquals(new BigDecimal("1.00"), ColisaoHelper.getTempoColisao(new BigDecimal("3"), 3));
    }

}