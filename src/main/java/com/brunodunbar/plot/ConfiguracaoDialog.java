package com.brunodunbar.plot;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ConfiguracaoDialog extends Dialog<Void> {


    public ConfiguracaoDialog(Configuracao configuracao) {

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        setTitle("Configurações");

        setHeaderText("Informe as configurações");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Distancia mínima aeroporto:"), 0, 0);
        TextField distanciaMinAeroportoField = new TextField(Double.toString(configuracao.getDistanciaMinAeroporto()));
        grid.add(distanciaMinAeroportoField, 1, 0);

        grid.add(new Label("Distancia mínima aviões:"), 0, 1);
        TextField distanciaMinAvioesField = new TextField(Double.toString(configuracao.getDistanciaMinAvioes()));
        grid.add(distanciaMinAvioesField, 1, 1);

        grid.add(new Label("Intervalo mínimo:"), 0, 2);
        TextField intervaloMinField = new TextField(Double.toString(configuracao.getIntervaloMin()));
        grid.add(intervaloMinField, 1, 2);

        getDialogPane().setContent(grid);
        Platform.runLater(distanciaMinAeroportoField::requestFocus);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                configuracao.setDistanciaMinAeroporto(Double.valueOf(distanciaMinAeroportoField.getText()));
                configuracao.setDistanciaMinAvioes(Double.valueOf(distanciaMinAvioesField.getText()));
                configuracao.setIntervaloMin(Double.valueOf(intervaloMinField.getText()));
            }

            return null;
        });
    }
}
