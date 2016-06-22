package com.brunodunbar.plot;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @FXML
    private TableView<Aviao> objetosTable;

    private FileChooser salvarFileChooser;
    private FileChooser abrirFileChooser;

    private ObjectProperty<Aviao> aviaoSelecionado = new SimpleObjectProperty<>();

    private Configuracao configuracao = new Configuracao();

    @FXML
    public void initialize() {

        TableColumn descricaoCol = new TableColumn("Descrição");
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn coordenadaCol = new TableColumn("Coordenada");
        coordenadaCol.setPrefWidth(100);
        coordenadaCol.setCellValueFactory(new PropertyValueFactory<>("coordenada"));

        TableColumn direcaoCol = new TableColumn("Direção");
        direcaoCol.setPrefWidth(100);
        direcaoCol.setCellValueFactory(new PropertyValueFactory<>("direcao"));

        TableColumn velocidadeCol = new TableColumn("Velocidade");
        velocidadeCol.setPrefWidth(100);
        velocidadeCol.setCellValueFactory(new PropertyValueFactory<>("velocidade"));

        objetosTable.getColumns().addAll(descricaoCol, coordenadaCol, direcaoCol, velocidadeCol);
        objetosTable.setItems(plano.getAvioes());

        objetosTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            aviaoSelecionado.setValue(newValue);
        });

        aviaoSelecionado.addListener((observable, oldValue, newValue) -> {
            plano.getAvioes().forEach(objeto -> objeto.setSelecionado(false));
            if (newValue != null) {
                newValue.setSelecionado(true);
            }
        });
    }

    public void handleAddAviao(ActionEvent actionEvent) {
        new AviaoDialog(new Aviao()).showAndWait()
                .ifPresent(aviao -> plano.add(aviao));
    }

    public void handleEditAviao(ActionEvent actionEvent) {
        if (aviaoSelecionado.get() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Selecione um avião").show();
            return;
        }

        new AviaoDialog(aviaoSelecionado.get()).show();
    }


    public void handleDistanciaAeroporto(ActionEvent actionEvent) {
        for (Aviao aviao : plano.getAvioes()) {
            System.out.println("Avião: " + aviao.getDescricao() + " " + aviao.getCoordenada().asCartesiana().distance());
        }
    }

    public void handleDistanciaAvioes(ActionEvent actionEvent) {

        for (int i = 0; i < plano.getAvioes().size(); i++) {
            Aviao aviao1 = plano.getAvioes().get(i);

            for (int j = i + 1; j < plano.getAvioes().size(); j++) {
                Aviao aviao2 = plano.getAvioes().get(j);

                System.out.println(aviao1.getDescricao() + " " + aviao2.getDescricao() + " " +
                        aviao1.getCoordenada().asCartesiana().distance(aviao2.getCoordenada()));

            }
        }


    }

    public void handleEscalonar(ActionEvent actionEvent) {

        Dialog<Pair<Double, Double>> dialog = new Dialog<>();

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setTitle("Escalonar ponto");
        dialog.setHeaderText("Informe as porcentagens para escalonar");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField xTextField = new TextField();
        xTextField.setPromptText("porcentagem para X");

        TextField yTextField = new TextField();
        yTextField.setPromptText("porcentagem para Y");

        grid.add(new Label("X:"), 0, 0);
        grid.add(xTextField, 1, 0);
        grid.add(new Label("Y:"), 0, 1);
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
            //   plano.escalonarPonto(pair.getKey(), pair.getValue());
        });
    }

    public void handleRotacionar(ActionEvent actionEvent) {

        if (aviaoSelecionado.get() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Selecione um objeto").show();
            return;
        }

        new AnguloDialog().showAndWait().ifPresent(angle -> {
            Coordenada coordenada = aviaoSelecionado.get().getCoordenada();
            aviaoSelecionado.get().setCoordenada(coordenada.rotate(angle));
        });
    }

    public void handleConfiguracoes(ActionEvent actionEvent) {
        new ConfiguracaoDialog(configuracao).showAndWait();
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
                        case "avioes":

                            reader.beginArray();

                            while (reader.hasNext()) {

                                reader.beginObject();

                                Aviao aviao = new Aviao();

                                double x = 0, y = 0;

                                while (reader.hasNext()) {
                                    switch (reader.nextName()) {
                                        case "x":
                                            x = reader.nextDouble();
                                            break;
                                        case "y":
                                            y = reader.nextDouble();
                                            break;
                                        case "descricao":
                                            aviao.setDescricao(reader.nextString());
                                            break;
                                        case "direcao":
                                            aviao.setDirecao(reader.nextDouble());
                                            break;
                                        case "velocidade":
                                            aviao.setVelocidade(reader.nextDouble());
                                            break;
                                        default:
                                            reader.skipValue();
                                    }
                                }

                                aviao.setCoordenada(CoordenadaCartesiana.of(x, y));

                                plano.add(aviao);

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
                writer.name("avioes");

                writer.beginArray();

                for (Aviao aviao : plano.getAvioes()) {

                    writer.beginObject();

                    CoordenadaCartesiana coordenada = aviao.getCoordenada().asCartesiana();

                    writer.name("descricao").value(aviao.getDescricao());

                    writer.name("x").value(coordenada.getX());
                    writer.name("y").value(coordenada.getY());

                    writer.name("direcao").value(aviao.getDirecao());
                    writer.name("velocidade").value(aviao.getVelocidade());

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
