package com.brunodunbar.plot;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.math.BigDecimal;

public class CoordenadaCartesiana extends Coordenada {

    public static final CoordenadaCartesiana ZERO = new CoordenadaCartesiana(BigDecimal.ZERO, BigDecimal.ZERO);

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
