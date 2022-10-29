package com.quesssystems.rpaconsultaprodutos.automacao;

import automacao.Pendencia;

import java.util.ArrayList;
import java.util.List;

public class PendenciaConsultaProdutos extends Pendencia {
    private String nomeProduto;

    private final List<String> links = new ArrayList<>();

    private final List<String> valores = new ArrayList<>();

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public List<String> getLinks() {
        return links;
    }

    public void addLink(String link) {
        links.add(link);
    }

    public List<String> getValores() {
        return valores;
    }

    public void addValor(String valor) {
        valores.add(valor);
    }

    public void zerarInfosExtraidas() {
        links.clear();
        valores.clear();
    }
}
