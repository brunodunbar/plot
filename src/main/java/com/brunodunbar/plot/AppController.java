package com.brunodunbar.plot;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class AppController {

    @FXML
    private Plano plano;
    private FileChooser salvarFileChooser;
    private FileChooser abrirFileChooser;

    @FXML
    public void initialize() {

    }

    public void handleInserirPonto(ActionEvent actionEvent) {

        Dialog<Pair<Double, Double>> dialog = new Dialog<>();

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setTitle("Inserir ponto");
        dialog.setHeaderText("Informe as coordenadas do ponto");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField xTextField = new TextField();
        xTextField.setPromptText("Coordenada X");
//        xTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                xTextField.setText(newValue.replaceAll("[^\\d]", ""));
//            }
//        });

        TextField yTextField = new TextField();
        yTextField.setPromptText("Coordenada Y");

        grid.add(new Label("Coordenada X:"), 0, 0);
        grid.add(xTextField, 1, 0);
        grid.add(new Label("Coordenada Y:"), 0, 1);
        grid.add(yTextField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(xTextField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(Double.valueOf(xTextField.getText()), Double.valueOf(yTextField.getText()));
            }
            return null;
        });

        Optional<Pair<Double, Double>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            plano.addPonto(pair.getKey(), pair.getValue());
        });
    }

    public void handleAbrir(ActionEvent actionEvent) {

        if (abrirFileChooser == null) {
            abrirFileChooser = new FileChooser();
            abrirFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json", "*.json"));
            abrirFileChooser.setTitle("Abrir...");
        }

        File file = abrirFileChooser.showOpenDialog(plano.getScene().getWindow());

        if (file != null && file.exists()) {
            try (JsonReader reader = new JsonReader(new FileReader(file))) {

                plano.clear();

                reader.beginObject();

                while (reader.hasNext()) {

                    switch (reader.nextName()) {
                        case "pontos":

                            reader.beginArray();

                            while (reader.hasNext()) {

                                reader.beginObject();

                                double x = 0, y = 0;

                                while (reader.hasNext()) {
                                    switch (reader.nextName()) {
                                        case "x":
                                            x = reader.nextDouble();
                                            break;
                                        case "y":
                                            y = reader.nextDouble();
                                            break;
                                        default:
                                            reader.skipValue();
                                    }
                                }

                                plano.addPonto(x, y);

                                reader.endObject();
                            }

                            reader.endArray();

                            break;

                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleSalvar(ActionEvent actionEvent) {
        if (salvarFileChooser == null) {
            salvarFileChooser = new FileChooser();
            salvarFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json", "*.json"));
            salvarFileChooser.setTitle("Salvar...");
        }

        File file = salvarFileChooser.showSaveDialog(plano.getScene().getWindow());
        if (file != null) {

            try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
                writer.setIndent("  ");
                writer.beginObject();
                writer.name("pontos");

                writer.beginArray();

                for (Ponto ponto : plano.getPontos()) {

                    writer.beginObject();

                    Point2D point = plano.translate(ponto);

                    writer.name("x").value(point.getX());
                    writer.name("y").value(point.getY());

                    writer.endObject();
                }

                writer.endArray();
                writer.endObject();

                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
