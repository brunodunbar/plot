package com.brunodunbar.plot;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.math.BigDecimal;

public class Ponto extends VBox {

    private final BooleanProperty selecionado = new SimpleBooleanProperty();

    private final Label label;
    private final Plano plano;
    private final Circle circle;

    private Group group;


    private Point2D position;

    public Ponto(Plano plano) {
        this.plano = plano;

        setAlignment(Pos.CENTER);

        label = new Label();
//        getChildren().add(label);

        circle = new Circle();
        getChildren().add(circle);
        circle.setRadius(5);

        InvalidationListener updateLabel = observable1 -> {

            Point2D translate = plano.translate(this);

            BigDecimal x = BigDecimal.valueOf(translate.getX()).setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal y = BigDecimal.valueOf(translate.getY()).setScale(2, BigDecimal.ROUND_DOWN);


            position = new Point2D(x.doubleValue(), y.doubleValue());

            label.setText(x + ", " + y);
        };

        layoutXProperty().addListener(updateLabel);
        layoutYProperty().addListener(updateLabel);


        getStyleClass().add("ponto");

        selecionado.addListener(observable -> {
            if (selecionado.get()) {
                getStyleClass().add("selecionado");
            } else {
                getStyleClass().remove("selecionado");
            }
        });
    }


    public Point2D getPosition() {
        return position;
    }

    public double getOffsetX() {
        return 5;
    }

    public double getOffsetY() {
        return 5;
    }

    public void setLabel(String value) {
        label.setText(value);
    }

    public String getLabel() {
        return label.getText();
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado.set(selecionado);
    }

    public boolean isSelecionado() {
        return selecionado.get();
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return label.getText();
    }
}
