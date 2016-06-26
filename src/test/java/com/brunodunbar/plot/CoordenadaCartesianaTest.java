package com.brunodunbar.plot;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class CoordenadaCartesianaTest {

    @Test
    public void deveRetornarOCoeficienteAngular() {
        Assert.assertEquals(new BigDecimal("1.00"), CoordenadaCartesiana.of(10, 10).getCoeficienteAngular(45));
    }

    @Test
    public void deveRetornarOCoeficienteLinear() {
        CoordenadaCartesiana cartesiana = CoordenadaCartesiana.of(6, -5);
        BigDecimal coeficienteAngular = BigDecimal.valueOf(2D / 3D);
        Assert.assertEquals(new BigDecimal("-9.00"), cartesiana.getCoeficienteLinear(coeficienteAngular));
    }

    @Test
    public void deveCalcularOValorCorreto() {
        BigDecimal y = BigDecimal.valueOf(2D / 3D).multiply(new BigDecimal("6")).add(BigDecimal.valueOf(-9)).setScale(2, BigDecimal.ROUND_HALF_UP);
        Assert.assertEquals(new BigDecimal("-5.00"), y);
    }
}