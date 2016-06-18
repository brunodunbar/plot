package com.brunodunbar.plot;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class CoordenadaPolarDialog extends Dialog<CoordenadaPolar> {

    public CoordenadaPolarDialog() {

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        setTitle("Coordenada polar");
        setHeaderText("Informe a coordenada polar");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField radiusTextField = new TextField();
        radiusTextField.setPromptText("Raio");

        TextField angleTextField = new TextField();
        angleTextField.setPromptText("Angulo");

        grid.add(new Label("Raio:"), 0, 0);
        grid.add(radiusTextField, 1, 0);
        grid.add(new Label("Angulo:"), 0, 1);
        grid.add(angleTextField, 1, 1);

        getDialogPane().setContent(grid);
        Platform.runLater(radiusTextField::requestFocus);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return CoordenadaPolar.of(radiusTextField.getText(), angleTextField.getText());
            }
            return null;
        });
    }
}
