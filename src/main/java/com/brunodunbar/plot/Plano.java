package com.brunodunbar.plot;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.io.IOException;

public class Plano extends GridPane {

    @FXML
    private Pane planoPane;

    private ObservableList<Aviao> avioes = FXCollections.observableArrayList();

    public Plano() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Plano.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        avioes.addListener((ListChangeListener<Aviao>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    planoPane.getChildren().removeAll(c.getRemoved());
                }
                if (c.wasAdded()) {
                    planoPane.getChildren().addAll(c.getAddedSubList());
                }
            }
        });

        Line line1 = new Line();
        Line line2 = new Line();

        line1.setStrokeWidth(2);
        line2.setStrokeWidth(2);

        planoPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            line1.setStartX(newValue.doubleValue() / 2);
            line1.setEndX(newValue.doubleValue() / 2);

            line2.setEndX(newValue.doubleValue());

        });
        planoPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            line1.setEndY(newValue.doubleValue());

            line2.setStartY(newValue.doubleValue() / 2);
            line2.setEndY(newValue.doubleValue() / 2);
        });

        planoPane.getChildren().addAll(line1, line2);
    }

    public void add(Aviao aviao) {
        avioes.add(aviao);
    }

    public ObservableList<Aviao> getAvioes() {
        return avioes;
    }

    public void clear() {
        avioes.clear();
    }
}
