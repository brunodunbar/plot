package com.brunodunbar.plot;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AppController {

    @FXML
    private Plano plano;
    private FileChooser salvarFileChooser;
    private FileChooser abrirFileChooser;

    @FXML
    public void initialize() {

    }

    public void handleCalcular(ActionEvent actionEvent) {
//        try {
//            floydWarshall.calcular();
//        } catch (IllegalStateException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Falha ao calcular caminho");
//            alert.setHeaderText(null);
//            alert.setContentText(e.getMessage());
//
//            alert.showAndWait();
//        }
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

//                plano.clear();
//
//                reader.beginObject();
//
//                while (reader.hasNext()) {
//
//                    switch (reader.nextName()) {
//                        case "nos":
//
//                            reader.beginArray();
//
//                            while (reader.hasNext()) {
//
//                                reader.beginObject();
//
//                                Ponto no = new Ponto();
//
//                                boolean noFinal = false, noInicial = false;
//
//                                while (reader.hasNext()) {
//                                    switch (reader.nextName()) {
//                                        case "label":
//                                            no.setLabel(reader.nextString());
//                                            break;
//                                        case "x":
//                                            no.setLayoutX(reader.nextDouble());
//                                            break;
//                                        case "y":
//                                            no.setLayoutY(reader.nextDouble());
//                                            break;
//                                        case "inicial":
//                                            noInicial = reader.nextBoolean();
//                                            break;
//                                        case "final":
//                                            noFinal = reader.nextBoolean();
//                                            break;
//                                        default:
//                                            reader.skipValue();
//                                    }
//                                }
//
//                                plano.getPontos().add(no);
//
//                                if (noInicial) {
//                                    plano.setNoInicial(no);
//                                }
//
//                                if (noFinal) {
//                                    plano.setNoFinal(no);
//                                }
//
//                                reader.endObject();
//                            }
//
//                            reader.endArray();
//
//                            break;
//                        case "arestas":
//
//                            reader.beginArray();
//
//                            while (reader.hasNext()) {
//
//                                reader.beginObject();
//
//                                Ponto de = null, para = null;
//                                int distancia = 0;
//
//                                while (reader.hasNext()) {
//                                    switch (reader.nextName()) {
//                                        case "de":
//                                            de = plano.buscaNoPorLabel(reader.nextString());
//                                            break;
//                                        case "para":
//                                            para = plano.buscaNoPorLabel(reader.nextString());
//                                            break;
//                                        case "distancia":
//                                            distancia = reader.nextInt();
//                                            break;
//                                        default:
//                                            reader.skipValue();
//                                    }
//                                }
//
//                                Aresta aresta = new Aresta(de, para);
//                                aresta.setDistancia(distancia);
//
//                                plano.getArestas().add(aresta);
//
//                                reader.endObject();
//                            }
//                            reader.endArray();
//                            break;
//                        default:
//                            reader.skipValue();
//                    }
//                }
//
//                reader.endObject();

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
//                writer.setIndent("  ");
//                writer.beginObject();
//                writer.name("nos");
//
//                writer.beginArray();
//
//                for (Ponto no : plano.getPontos()) {
//
//                    writer.beginObject();
//
//                    writer.name("label").value(no.getLabel());
//                    writer.name("x").value(no.getLayoutX());
//                    writer.name("y").value(no.getLayoutY());
//
//                    if (no.getInicial()) {
//                        writer.name("inicial").value(no.getInicial());
//                    }
//
//                    if (no.getFinal()) {
//                        writer.name("final").value(no.getFinal());
//                    }
//
//                    writer.endObject();
//
//                }
//                writer.endArray();
//
//                writer.name("arestas");
//                writer.beginArray();
//                for (Aresta aresta : plano.getArestas()) {
//
//                    writer.beginObject();
//
//                    writer.name("de").value(aresta.getDe().getLabel());
//                    writer.name("para").value(aresta.getPara().getLabel());
//                    writer.name("distancia").value(aresta.getLabel());
//
//                    writer.endObject();
//                }
//                writer.endArray();
//
//                writer.endObject();

                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
