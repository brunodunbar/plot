package com.brunodunbar.plot;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Configuracao {

    private DoubleProperty distanciaMinAeroporto = new SimpleDoubleProperty(0.2);

    public double getDistanciaMinAeroporto() {
        return distanciaMinAeroporto.get();
    }

    public DoubleProperty distanciaMinAeroportoProperty() {
        return distanciaMinAeroporto;
    }

    public void setDistanciaMinAeroporto(double distanciaMinAeroporto) {
        this.distanciaMinAeroporto.set(distanciaMinAeroporto);
    }
}
