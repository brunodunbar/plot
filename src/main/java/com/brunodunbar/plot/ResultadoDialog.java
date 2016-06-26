package com.brunodunbar.plot;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ResultadoDialog extends Dialog<Void> {

    public ResultadoDialog(String resultado) {

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType);

        setTitle("Resultado");
        setHeaderText("Resultado");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextArea xTextField = new TextArea(resultado);
        grid.add(xTextField, 1, 0);

        getDialogPane().setContent(grid);
        Platform.runLater(xTextField::requestFocus);
    }
}
