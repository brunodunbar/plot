package com.brunodunbar.plot;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AviaoTest {


    //@Test
    public void test() {

        BigDecimal angular = new BigDecimal("3");
        BigDecimal linear = new BigDecimal("-9");

        BigDecimal outroAngular = new BigDecimal("2");
        BigDecimal outroLinear = new BigDecimal("5");

        assertEquals(new BigDecimal("-14"), linear.subtract(outroLinear).divide(angular.subtract(outroAngular)));

    }



}