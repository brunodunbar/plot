package com.brunodunbar.plot;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class CoordenadaCartesianaDialog extends Dialog<CoordenadaCartesiana> {

    public CoordenadaCartesianaDialog() {
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        setTitle("Coordenada cartesiana");
        setHeaderText("Informe a coordenada cartesiana");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField xTextField = new TextField();
        xTextField.setPromptText("Coordenada X");

        TextField yTextField = new TextField();
        yTextField.setPromptText("Coordenada Y");

        grid.add(new Label("Coordenada X:"), 0, 0);
        grid.add(xTextField, 1, 0);
        grid.add(new Label("Coordenada Y:"), 0, 1);
        grid.add(yTextField, 1, 1);

        getDialogPane().setContent(grid);
        Platform.runLater(xTextField::requestFocus);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return CoordenadaCartesiana.of(xTextField.getText(), yTextField.getText());
            }
            return null;
        });
    }
}
