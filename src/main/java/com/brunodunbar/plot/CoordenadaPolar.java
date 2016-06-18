package com.brunodunbar.plot;

import java.math.BigDecimal;

public class CoordenadaPolar extends Coordenada {

    private final BigDecimal radius, angle;

    private CoordenadaPolar(BigDecimal radius, BigDecimal angle) {
        super(Tipo.POLAR);
        this.radius = radius.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.angle = angle.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static CoordenadaPolar of(String radius, String angle) {
        return new CoordenadaPolar(new BigDecimal(radius), new BigDecimal(angle));
    }

    @Override
    public Coordenada rotate(Double angle) {
        return new CoordenadaPolar(radius, this.angle.add(BigDecimal.valueOf(angle)));
    }

    @Override
    public CoordenadaCartesiana asCartesiana() {
        double radians = Math.toRadians(angle.doubleValue());
        return CoordenadaCartesiana.of(radius.doubleValue() * Math.cos(radians), radius.doubleValue() * Math.sin(radians));
    }

    public BigDecimal getRadius() {
        return radius;
    }

    public BigDecimal getAngle() {
        return angle;
    }

    @Override
    public String toString() {
        return "P(" + radius + ", " + angle + "º)";
    }
}
