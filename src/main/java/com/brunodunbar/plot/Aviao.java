package com.brunodunbar.plot;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.math.BigDecimal;

public class Aviao extends Objeto {

    private final StringProperty descricao = new SimpleStringProperty();

    private final DoubleProperty direcao = new SimpleDoubleProperty();

    private final DoubleProperty velocidade = new SimpleDoubleProperty();

    public Aviao() {
        Image image = new Image("plane.png");
        ImageView imageView = new ImageView(image);
        getChildren().add(imageView);

        direcao.addListener(observable -> {
            imageView.setRotate(360 - direcao.doubleValue());
        });

        getStyleClass().add("aviao");
    }

//    public BigDecimal getIntervaloColisao(Aviao aviao) {
//        CoordenadaCartesiana coordenadaColisao = getCoordenadaColisao(aviao);
//
//        BigDecimal tempo1 = ColisaoHelper.getTempoColisao(coordenadaColisao.distance(getCoordenada()), getVelocidade());
//        BigDecimal tempo2 = ColisaoHelper.getTempoColisao(coordenadaColisao.distance(aviao.getCoordenada()), aviao.getVelocidade());
//
//        return tempo1.subtract(tempo2).abs();
//    }

//    public CoordenadaCartesiana getCoordenadaColisao(Aviao aviao) {
//        return ColisaoHelper.getCoordenadaColisao(getCoordenada(), getDirecao(), aviao.getCoordenada(), aviao.getDirecao());
//    }

    private double getCoeficienteLinear() {
        double angular = getCoeficienteAngular();
        CoordenadaCartesiana cartesiana = getCoordenada().asCartesiana();
        return cartesiana.getY().doubleValue() - angular * cartesiana.getX().doubleValue();
    }

    private double getCoeficienteAngular() {
        return Math.tan(Math.toRadians(direcao.doubleValue()));
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
