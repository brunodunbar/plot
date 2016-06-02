package com.brunodunbar.plot;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class Ponto extends VBox {

    private Label label;
    private final BooleanProperty selecionado = new SimpleBooleanProperty();

    private Group group;

    public Ponto() {

        setAlignment(Pos.CENTER);

        label = new Label();
        getChildren().add(label);

        Circle circle = new Circle();
        getChildren().add(circle);
        circle.setRadius(5);

        InvalidationListener updateLabel = observable1 -> {
            label.setText(getLayoutX() + ", " + getLayoutY());
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
