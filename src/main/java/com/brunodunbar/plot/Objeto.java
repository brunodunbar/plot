package com.brunodunbar.plot;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public abstract class Objeto extends VBox {

    private final BooleanProperty selecionado = new SimpleBooleanProperty();
    private final ObjectProperty<Coordenada> coordenada;
    private final StringProperty descricao;

    public Objeto(Coordenada coordenada) {
        this.coordenada = new SimpleObjectProperty<>(coordenada);
        this.descricao = new SimpleStringProperty();

        setAlignment(Pos.TOP_CENTER);

        //this.plano = plano;

//        InvalidationListener updateLabel = observable1 -> {
//
//            Point2D translate = plano.translate(this);
//
//            BigDecimal x = BigDecimal.valueOf(translate.getX()).setScale(2, BigDecimal.ROUND_DOWN);
//            BigDecimal y = BigDecimal.valueOf(translate.getY()).setScale(2, BigDecimal.ROUND_DOWN);
//
//            position = new Point2D(x.doubleValue(), y.doubleValue());
//
//        };

        //  layoutXProperty().addListener(updateLabel);
        //  layoutYProperty().addListener(updateLabel);

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

            //Bounds boundsInParent = getBoundsInParent();

            System.out.println("---- Parent bounds: " + parent.getLayoutBounds());
            System.out.println("---- Width: " + getWidth() + ", Height: " + getHeight());
            System.out.println("---- Point: " + point);

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

    public String getDescricao() {
        return descricao.get();
    }

    public StringProperty descricaoProperty() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }
}
