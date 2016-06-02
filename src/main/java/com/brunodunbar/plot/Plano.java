package com.brunodunbar.plot;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Plano extends GridPane {

    private double actionX, actionY;

    private Ponto actionPonto;

    private Ponto pontoSelecionado;

    @FXML
    private Pane planoPane;

    private ObservableList<Ponto> pontos = FXCollections.observableArrayList();
    private ObservableList<Aresta> arestas = FXCollections.observableArrayList();

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
                    //Remove as arestas dos nós que estão sendo removidos
                    c.getRemoved().forEach(o -> {
                        System.out.print("Removendo nó: " + o);
                        arestas.removeAll(buscaArestas(o).collect(Collectors.toList()));
                    });

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

        arestas.addListener((ListChangeListener<Aresta>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    planoPane.getChildren().removeAll(c.getRemoved());
                }
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(o -> o.setPlano(this));
                    planoPane.getChildren().addAll(c.getAddedSubList());
                }
            }
        });


        contextMenu = new ContextMenu();
        MenuItem novoNo = new MenuItem("Novo Nó");
        novoNo.setOnAction(e -> {
            Ponto ponto = new Ponto();

            ponto.setLayoutX(actionX);
            ponto.setLayoutY(actionY);

            //ponto.setLabel("Nó " + (pontos.size() + 1));

            pontos.add(ponto);
        });
        contextMenu.getItems().addAll(novoNo);

        noContextMenu = new ContextMenu();

        MenuItem criarAresta = new MenuItem("Criar aresta");
        criarAresta.setOnAction(event -> {
            if (pontoSelecionado != null && pontoSelecionado != actionPonto) {
                if (!possuiAresta(pontoSelecionado, actionPonto)) {
                    Aresta aresta = new Aresta(pontoSelecionado, actionPonto);
                    arestas.add(aresta);
                }
            }
        });

//        MenuItem definirInicial = new MenuItem("Definir como nó inicial");
//        definirInicial.setOnAction(event -> setNoInicial(actionPonto));
//
//        MenuItem definirFinal = new MenuItem("Definir como nó final");
//        definirFinal.setOnAction(event -> setNoFinal(actionPonto));

        MenuItem removerNo = new MenuItem("Remover nó");
        removerNo.setOnAction(event -> pontos.remove(actionPonto));

        noContextMenu.getItems().addAll(criarAresta, removerNo);

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

    private boolean possuiAresta(Ponto ponto1, Ponto ponto2) {
        return buscaAresta(ponto1, ponto2).isPresent();
    }

    private Optional<Aresta> buscaAresta(Ponto ponto1, Ponto ponto2) {
        return arestas.stream().filter(aresta -> aresta.getDe().equals(ponto1) && aresta.getPara().equals(ponto2) ||
                aresta.getDe().equals(ponto2) && aresta.getPara().equals(ponto1))
                .findAny();
    }

    private class Delta {
        double x, y;
    }

    public ObservableList<Ponto> getPontos() {
        return pontos;
    }

    public ObservableList<Aresta> getArestas() {
        return arestas;
    }

    public Stream<Aresta> buscaArestas(Ponto ponto) {
        return arestas.stream().filter(aresta -> Objects.equals(aresta.getDe(), ponto) || Objects.equals(aresta.getPara(), ponto));
    }

    public void clear() {
        arestas.clear();
        pontos.clear();
        planoPane.getChildren().clear();
    }

//    public Ponto getNoInicial() {
//        return noInicial;
//    }
//
//    public Ponto getNoFinal() {
//        return noFinal;
//    }
}
