package com.brunodunbar.plot;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public abstract class Objeto extends VBox {

    private static final Logger LOGGER = LogManager.getLogger(Objeto.class);

    private final BooleanProperty selecionado = new SimpleBooleanProperty();
    private final ObjectProperty<Coordenada> coordenada = new SimpleObjectProperty<>();

    public Objeto() {
        setAlignment(Pos.TOP_CENTER);

        this.coordenada.addListener(observable -> updatePosition());
        this.parentProperty().addListener(observable -> updatePosition());
        this.widthProperty().addListener(observable -> updatePosition());
        this.heightProperty().addListener(observable -> updatePosition());

        this.selecionado.addListener(observable -> {
            if (selecionado.get()) {
                getStyleClass().add("selecionado");
            } else {
                getStyleClass().remove("selecionado");
            }
        });
    }

    private void updatePosition() {
        Optional.ofNullable(getParent()).ifPresent(parent -> {
            Point2D point = getCoordenada().asCartesiana().toPoint(parent);

            LOGGER.debug("Parent bounds: " + parent.getLayoutBounds());
            LOGGER.debug("Width: " + getWidth() + ", Height: " + getHeight());
            LOGGER.debug("Point: " + point);

            setLayoutX(point.getX() - getWidth() / 2);
            setLayoutY(point.getY() - getHeight() / 2);
        });
    }


    public Coordenada getCoordenada() {
        return coordenada.get();
    }

    public ObjectProperty<Coordenada> coordenadaProperty() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada.set(coordenada);
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado.set(selecionado);
    }

    public boolean isSelecionado() {
        return selecionado.get();
    }

    public BooleanProperty selecionadoProperty() {
        return selecionado;
    }
}
