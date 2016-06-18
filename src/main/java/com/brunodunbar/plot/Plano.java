package com.brunodunbar.plot;

import javafx.application.Platform;
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

    private double actionX, actionY;

    private Objeto actionObjeto;
    private Objeto objetoSelecionado;

    @FXML
    private Pane planoPane;

    private ObservableList<Objeto> objetos = FXCollections.observableArrayList();

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

        objetos.addListener((ListChangeListener<Objeto>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    planoPane.getChildren().removeAll(c.getRemoved());
                }
                if (c.wasAdded()) {
                    planoPane.getChildren().addAll(c.getAddedSubList());
                }
            }
        });

        contextMenu = new ContextMenu();

        MenuItem novoNo = new MenuItem("Novo ponto");
        novoNo.setOnAction(e -> {
            add(new Ponto(CoordenadaCartesiana.of(new Point2D( actionX, actionY), Plano.this)));
        });

        contextMenu.getItems().addAll(novoNo);

        noContextMenu = new ContextMenu();

        MenuItem removerPonto = new MenuItem("Remover ponto");
        removerPonto.setOnAction(event -> objetos.remove(actionObjeto));

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

//
//    public void addPontoPolar(Double angulo, Double raio) {
//        addPontoCartesiana(raio * Math.cos(Math.toRadians(angulo)), raio * Math.sin(Math.toRadians(angulo)));
//    }

//
//    public void moverPontoCartesiana(Double x, Double y) {
//
//        Point2D translate = translate(x, y);
//
//        if (objetoSelecionado != null) {
//            objetoSelecionado.setLayoutX(translate.getX() - objetoSelecionado.getOffsetX());
//            objetoSelecionado.setLayoutY(translate.getY() - objetoSelecionado.getOffsetY());
//        }
//    }

//    public void escalonarPonto(Double percX, Double percY) {
//
//        if (objetoSelecionado != null) {
//
//            Point2D position = objetoSelecionado.getPosition();
//
//            Point2D translate = translate(position.getX() * percX / 100, position.getY() * percY / 100);
//
//            objetoSelecionado.setLayoutX(translate.getX() - objetoSelecionado.getOffsetX());
//            objetoSelecionado.setLayoutY(translate.getY() - objetoSelecionado.getOffsetY());
//        }
//    }

//    public void rotacionarPonto(Double angulo) {
//
//        if (objetoSelecionado != null) {
//
//
//            Point2D position = objetoSelecionado.getPosition();
//
//            double radians = Math.toRadians(angulo);
//
//            Point2D translate = translate(position.getX() * Math.cos(radians) - position.getY() * Math.sin(radians),
//                    position.getY() * Math.cos(radians) + position.getX() * Math.sin(radians));
//
//            objetoSelecionado.setLayoutX(translate.getX() - objetoSelecionado.getOffsetX());
//            objetoSelecionado.setLayoutY(translate.getY() - objetoSelecionado.getOffsetY());
//        }
//
//
//    }

    public void add(Objeto objeto) {
//        Point2D point = coordenada.asCartesiana().toPoint(this);
//
//        objeto.setLayoutX(point.getX());
//        objeto.setLayoutY(point.getY());

        objetos.add(objeto);
    }

    public ObservableList<Objeto> getObjetos() {
        return objetos;
    }

    public void clear() {
        objetos.clear();
    }
}
