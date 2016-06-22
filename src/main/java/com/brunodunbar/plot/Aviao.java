package com.brunodunbar.plot;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Aviao extends Objeto {

    private final StringProperty descricao = new SimpleStringProperty();

    private final DoubleProperty direcao = new SimpleDoubleProperty();

    private final DoubleProperty velocidade = new SimpleDoubleProperty();

    public Aviao() {
        Image image = new Image("plane.png");
        ImageView imageView = new ImageView(image);
        getChildren().add(imageView);

        direcao.addListener(observable -> {
            imageView.setRotate(direcao.doubleValue());
        });

        getStyleClass().add("aviao");
    }

    public String getDescricao() {
        return descricao.get();
    }

    public StringProperty descricaoProperty() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }

    public double getDirecao() {
        return direcao.get();
    }

    public DoubleProperty direcaoProperty() {
        return direcao;
    }

    public void setDirecao(double direcao) {
        this.direcao.set(direcao);
    }

    public double getVelocidade() {
        return velocidade.get();
    }

    public DoubleProperty velocidadeProperty() {
        return velocidade;
    }

    public void setVelocidade(double velocidade) {
        this.velocidade.set(velocidade);
    }
}
