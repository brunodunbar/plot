package com.brunodunbar.plot;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

public class Plano extends GridPane {

    private double actionX, actionY;

    private Ponto actionPonto;
    private Ponto pontoSelecionado;

    @FXML
    private Pane planoPane;

    private ObservableList<Ponto> pontos = FXCollections.observableArrayList();

    private final ContextMenu contextMenu;
    private final ContextMenu noContextMenu;

    public Plano() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Plano.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        pontos.addListener((ListChangeListener<Ponto>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    //Remove os nós
                    List<Node> collect = c.getRemoved().stream()
                            .map(Ponto::getGroup)
                            .collect(Collectors.toList());

                    planoPane.getChildren().removeAll(collect);
                }
                if (c.wasAdded()) {
                    List<Node> collect = c.getAddedSubList().stream()
                            .map(this::makeDraggable)
                            .collect(Collectors.toList());

                    planoPane.getChildren().addAll(collect);
                }
            }
        });

        contextMenu = new ContextMenu();
        MenuItem novoNo = new MenuItem("Novo ponto");
        novoNo.setOnAction(e -> {
            addPontoCartesiana(new Point2D(actionX, actionY));
        });
        contextMenu.getItems().addAll(novoNo);

        noContextMenu = new ContextMenu();

//        MenuItem definirInicial = new MenuItem("Definir como nó inicial");
//        definirInicial.setOnAction(event -> setNoInicial(actionPonto));
//
//        MenuItem definirFinal = new MenuItem("Definir como nó final");
//        definirFinal.setOnAction(event -> setNoFinal(actionPonto));

        MenuItem removerPonto = new MenuItem("Remover ponto");
        removerPonto.setOnAction(event -> pontos.remove(actionPonto));

        noContextMenu.getItems().addAll(removerPonto);

        planoPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                actionX = event.getX();
                actionY = event.getY();

                contextMenu.show(Plano.this, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
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

    private Node makeDraggable(final Ponto ponto) {
        final Delta dragDelta = new Delta();
        final Group wrapGroup = new Group(ponto);
        ponto.setGroup(wrapGroup);

        wrapGroup.addEventFilter(MouseEvent.ANY, Event::consume);

        wrapGroup.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                actionPonto = ponto;
                noContextMenu.show(ponto, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            } else {
                noContextMenu.hide();
            }
        });

        wrapGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {

                if (pontoSelecionado != null) {
                    pontoSelecionado.setSelecionado(false);
                }
                pontoSelecionado = ponto;
                pontoSelecionado.setSelecionado(true);

                dragDelta.x = ponto.getLayoutX() - mouseEvent.getX();
                dragDelta.y = ponto.getLayoutY() - mouseEvent.getY();
                getScene().setCursor(Cursor.MOVE);
            }

            contextMenu.hide();
        });

        wrapGroup.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                getScene().setCursor(Cursor.HAND);
            }
        });

        wrapGroup.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                double newX = mouseEvent.getX() + dragDelta.x;
                if (newX > 0 && newX < getScene().getWidth() - ponto.getWidth()) {
                    ponto.setLayoutX(newX);
                }
                double newY = mouseEvent.getY() + dragDelta.y;
                if (newY > 0 && newY < getScene().getHeight() - ponto.getHeight()) {
                    ponto.setLayoutY(newY);
                }
            }
        });

        wrapGroup.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.HAND);
            }
        });

        wrapGroup.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });

        return wrapGroup;
    }

    public void addPontoPolar(Double angulo, Double raio) {
        addPontoCartesiana(raio * Math.cos(Math.toRadians(angulo)), raio * Math.sin(Math.toRadians(angulo)));
    }

    public void moverPontoCartesiana(Double x, Double y) {

        Point2D translate = translate(x, y);

        if (pontoSelecionado != null) {
            pontoSelecionado.setLayoutX(translate.getX() - pontoSelecionado.getOffsetX());
            pontoSelecionado.setLayoutY(translate.getY() - pontoSelecionado.getOffsetY());
        }
    }

    public void escalonarPonto(Double percX, Double percY) {

        if (pontoSelecionado != null) {

            Point2D position = pontoSelecionado.getPosition();

            Point2D translate = translate(position.getX() * percX / 100, position.getY() * percY / 100);

            pontoSelecionado.setLayoutX(translate.getX() - pontoSelecionado.getOffsetX());
            pontoSelecionado.setLayoutY(translate.getY() - pontoSelecionado.getOffsetY());
        }
    }

    public void rotacionarPonto(Double angulo) {

        if (pontoSelecionado != null) {


            Point2D position = pontoSelecionado.getPosition();

            double radians = Math.toRadians(angulo);

            Point2D translate = translate(position.getX() * Math.cos(radians) - position.getY() * Math.sin(radians),
                    position.getY() * Math.cos(radians) + position.getX() * Math.sin(radians));

            pontoSelecionado.setLayoutX(translate.getX() - pontoSelecionado.getOffsetX());
            pontoSelecionado.setLayoutY(translate.getY() - pontoSelecionado.getOffsetY());
        }


    }

    private class Delta {
        double x, y;
    }

    public ObservableList<Ponto> getPontos() {
        return pontos;
    }

    public void clear() {
        pontos.clear();
    }

    public void addPontoCartesiana(Double x, Double y) {
        addPontoCartesiana(translate(x, y));
    }

    public void addPontoCartesiana(Point2D point) {
        Ponto ponto = new Ponto(Plano.this);

        ponto.setLayoutX(point.getX());
        ponto.setLayoutY(point.getY());

        pontos.add(ponto);

        Platform.runLater(() -> {
            ponto.setLayoutX(point.getX() - ponto.getOffsetX());
            ponto.setLayoutY(point.getY() - ponto.getOffsetY());
        });

    }

    public Point2D translate(Double x, Double y) {

        double centerY = getHeight() / 2;
        double centerX = getWidth() / 2;

        double transX = centerX + x * (centerX / 10);
        double transY = centerY - y * (centerY / 10);


        return new Point2D(transX, transY);
    }

    public Point2D translate(Ponto ponto) {

        double centerY = getHeight() / 2;
        double centerX = getWidth() / 2;

        double x = ponto.getLayoutX() + ponto.getOffsetX();
        double y = ponto.getLayoutY() + ponto.getOffsetY();

        double transX = (x - centerX) / (centerX / 10);
        double transY = (centerY - y) / (centerY / 10);

        return new Point2D(transX, transY);
    }
}
