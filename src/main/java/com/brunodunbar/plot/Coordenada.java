package com.brunodunbar.plot;

public abstract class Coordenada {

    public enum Tipo {
        CARTESIANA, POLAR
    }

    private Tipo tipo;

    protected Coordenada(Tipo tipo) {
        this.tipo = tipo;
    }

    public abstract Coordenada rotate(Double angle);

    public CoordenadaCartesiana asCartesiana() {
        throw new UnsupportedOperationException();
    }

    public CoordenadaPolar asPolar() {
        throw new UnsupportedOperationException();
    }

    public Tipo getTipo() {
        return tipo;
    }
}
