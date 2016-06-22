package com.brunodunbar.plot;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class AviaoDialog extends Dialog<Aviao> {

    private Aviao aviao;

    public AviaoDialog(Aviao aviao) {
        this.aviao = aviao;

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        setTitle("Avião " + aviao.getDescricao());

        setHeaderText("Informe os dados do avião");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //Descrição
        grid.add(new Label("Descrição:"), 0, 0);
        TextField descricaoField = new TextField(aviao.getDescricao());
        grid.add(descricaoField, 1, 0);

        //Direção
        grid.add(new Label("Direção:"), 0, 1);
        TextField direcaoField = new TextField(Double.toString(aviao.getDirecao()));
        grid.add(direcaoField, 1, 1);

        //Velociada
        grid.add(new Label("Velocidade:"), 0, 2);
        TextField velocidadeField = new TextField(Double.toString(aviao.getVelocidade()));
        grid.add(velocidadeField, 1, 2);

        //Tipo coordenada
        final ToggleGroup group = new ToggleGroup();

        RadioButton polarRadio = new RadioButton("Coordenada polar");
        polarRadio.setToggleGroup(group);
        grid.add(polarRadio, 0, 3);

        RadioButton cartesianaRadio = new RadioButton("Coordenada cartesiana");
        cartesianaRadio.setToggleGroup(group);
        cartesianaRadio.setSelected(true);
        grid.add(cartesianaRadio, 1, 3);

        //Coordenada
        Label coord1Label = new Label("X:");
        grid.add(coord1Label, 0, 4);
        TextField coord1Field = new TextField();

        grid.add(coord1Field, 1, 4);

        Label coord2Label = new Label("Y:");
        grid.add(coord2Label, 0, 5);
        TextField coord2Field = new TextField();
        grid.add(coord2Field, 1, 5);

        if(aviao.getCoordenada() != null) {

            if(aviao.getCoordenada().getTipo() == Coordenada.Tipo.CARTESIANA) {
                cartesianaRadio.setSelected(true);
                coord1Field.setText(aviao.getCoordenada().asCartesiana().getX().toString());
                coord2Field.setText(aviao.getCoordenada().asCartesiana().getY().toString());
            } else {
                polarRadio.setSelected(true);
                coord1Field.setText(aviao.getCoordenada().asPolar().getRadius().toString());
                coord2Field.setText(aviao.getCoordenada().asPolar().getAngle().toString());
            }
        }

        group.selectedToggleProperty().addListener(observable -> {
            if (cartesianaRadio.isSelected()) {
                coord1Label.setText("X:");
                coord2Label.setText("Y:");
            } else {
                coord1Label.setText("Raio:");
                coord2Label.setText("Angulo:");
            }
        });

        getDialogPane().setContent(grid);
        Platform.runLater(descricaoField::requestFocus);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {

                aviao.setDescricao(descricaoField.getText());
                aviao.setDirecao(Double.valueOf(direcaoField.getText()));
                aviao.setVelocidade(Double.valueOf(velocidadeField.getText()));

                Coordenada coordenada;
                if (cartesianaRadio.isSelected()) {
                    coordenada = CoordenadaCartesiana.of(coord1Field.getText(), coord2Field.getText());
                } else {
                    coordenada = CoordenadaPolar.of(coord1Field.getText(), coord2Field.getText());
                }

                aviao.setCoordenada(coordenada);

                return aviao;
            }
            return null;
        });
    }
}
