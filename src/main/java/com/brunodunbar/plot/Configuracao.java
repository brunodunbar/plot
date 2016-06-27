package com.brunodunbar.plot;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Configuracao {

    private DoubleProperty distanciaMinAeroporto = new SimpleDoubleProperty(0.2);
    private DoubleProperty distanciaMinAvioes = new SimpleDoubleProperty(0.2);
    private DoubleProperty intervaloMin = new SimpleDoubleProperty(0.2);

    public double getDistanciaMinAeroporto() {
        return distanciaMinAeroporto.get();
    }

    public DoubleProperty distanciaMinAeroportoProperty() {
        return distanciaMinAeroporto;
    }

    public void setDistanciaMinAeroporto(double distanciaMinAeroporto) {
        this.distanciaMinAeroporto.set(distanciaMinAeroporto);
    }

    public double getDistanciaMinAvioes() {
        return distanciaMinAvioes.get();
    }

    public DoubleProperty distanciaMinAvioesProperty() {
        return distanciaMinAvioes;
    }

    public void setDistanciaMinAvioes(double distanciaMinAvioes) {
        this.distanciaMinAvioes.set(distanciaMinAvioes);
    }

    public double getIntervaloMin() {
        return intervaloMin.get();
    }

    public DoubleProperty intervaloMinProperty() {
        return intervaloMin;
    }

    public void setIntervaloMin(double intervaloMin) {
        this.intervaloMin.set(intervaloMin);
    }
}
