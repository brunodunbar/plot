package com.brunodunbar.plot;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;

public class AnguloDialog extends Dialog<Double> {

    public AnguloDialog() {

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        setTitle("Angulo");
        setHeaderText("Informe o angulo");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField xTextField = new TextField();
        xTextField.setPromptText("angulo");

        grid.add(new Label("Angulo:"), 0, 0);
        grid.add(xTextField, 1, 0);

        getDialogPane().setContent(grid);
        Platform.runLater(xTextField::requestFocus);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return Double.valueOf(xTextField.getText());
            }
            return null;
        });
    }
}
