package com.brunodunbar.plot;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.math.BigDecimal;

public class CoordenadaCartesiana extends Coordenada {

    public static final CoordenadaCartesiana ZERO = new CoordenadaCartesiana(BigDecimal.ZERO, BigDecimal.ZERO);
    public static final CoordenadaCartesiana INVALID = new CoordenadaCartesiana(BigDecimal.valueOf(Double.MAX_VALUE), BigDecimal.valueOf(Double.MAX_VALUE));


    private final BigDecimal x;
    private final BigDecimal y;

    private CoordenadaCartesiana(BigDecimal x, BigDecimal y) {
        super(Tipo.CARTESIANA);
        this.x = x.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.y = y.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    public static CoordenadaCartesiana of(double x, double y) {
        return new CoordenadaCartesiana(new BigDecimal(x), new BigDecimal(y));
    }

    public static CoordenadaCartesiana of(BigDecimal x, BigDecimal y) {
        return new CoordenadaCartesiana(x, y);
    }

    public static CoordenadaCartesiana of(String x, String y) {
        return new CoordenadaCartesiana(new BigDecimal(x), new BigDecimal(y));
    }

    public static CoordenadaCartesiana of(Point2D point, Region region) {

        double centerY = region.getHeight() / 2;
        double centerX = region.getWidth() / 2;

        double x = point.getX();
        double y = point.getY();

        double transX = (x - centerX) / (centerX / Constants.SCALE_X);
        double transY = (centerY - y) / (centerY / Constants.SCALE_Y);

        return new CoordenadaCartesiana(BigDecimal.valueOf(transX), BigDecimal.valueOf(transY));
    }

    public Point2D toPoint(Node node) {

        Bounds bounds = node.getLayoutBounds();

        double centerX = bounds.getWidth() / 2;
        double centerY = bounds.getHeight() / 2;

        double transX = centerX + x.doubleValue() * (centerX / Constants.SCALE_X);
        double transY = centerY - y.doubleValue() * (centerY / Constants.SCALE_Y);

        return new Point2D(transX, transY);
    }

    @Override
    public Coordenada rotate(Double angle) {
        double radians = Math.toRadians(angle);
        return new CoordenadaCartesiana(BigDecimal.valueOf(x.doubleValue() * Math.cos(radians) - y.doubleValue() * Math.sin(radians)),
                BigDecimal.valueOf(y.doubleValue() * Math.cos(radians) + x.doubleValue() * Math.sin(radians)));
    }

    public double distance() {
        return Math.sqrt(x.doubleValue() * x.doubleValue() + y.doubleValue() * y.doubleValue());
    }

    public BigDecimal distance(Coordenada coordenada) {

        CoordenadaCartesiana cartesiana = coordenada.asCartesiana();

        BigDecimal x = getX().subtract(cartesiana.getX());
        BigDecimal y = getY().subtract(cartesiana.getY());

        double sqrt = Math.sqrt(x.multiply(x).add(y.multiply(y)).doubleValue());

//        if(sqrt == Double.NaN) {
//            return
//        }

        return BigDecimal.valueOf(sqrt).setScale(Constants.PRESISION, BigDecimal.ROUND_HALF_UP);
    }


    public BigDecimal getCoeficienteLinear(BigDecimal coeficienteAngular) {
        return y.subtract(coeficienteAngular.multiply(x)).setScale(Constants.PRESISION, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCoeficienteAngular(double angle) {
        return BigDecimal.valueOf(Math.tan(Math.toRadians(angle))).setScale(Constants.PRESISION, BigDecimal.ROUND_HALF_UP);
    }

    public double getAngle(Coordenada target) {
        CoordenadaCartesiana cartesiana = target.asCartesiana();
        float angle = (float) Math.toDegrees(Math.atan2(cartesiana.getY().doubleValue() - getY().doubleValue(), cartesiana.getX().doubleValue() - getX().doubleValue()));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    @Override
    public CoordenadaCartesiana asCartesiana() {
        return this;
    }

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    @Override
    public String toString() {
        return "C(" + x + ", " + y + ')';
    }
}
