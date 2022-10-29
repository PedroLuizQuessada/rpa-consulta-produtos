package com.quesssystems.rpaconsultaprodutos.service;

import automacao.Planilha;
import com.quesssystems.rpaconsultaprodutos.automacao.PendenciaConsultaProdutos;
import com.quesssystems.rpaconsultaprodutos.exceptions.ProdutoNaoEncontradoException;
import exceptions.ElementoNaoEncontradoException;
import exceptions.GerarPlanilhaException;
import exceptions.TimerUtilException;
import exceptions.UrlInvalidaException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ConversorUtil;
import util.GoogleDriveUtil;
import util.SeleniumUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class EcommerceService {
    private final Logger logger = LoggerFactory.getLogger(EcommerceService.class);
    private final String nomeEcommerce;
    private final String link;

    public EcommerceService(String nomeEcommerce, String link) {
        this.nomeEcommerce = nomeEcommerce;
        this.link = link;
    }

    public void processarEcommerce(WebDriver webDriver, int numeroResultados, List<PendenciaConsultaProdutos> pendenciasConsultaProdutos, List<Planilha> planilhas, String googleDrivePathResultados) throws UrlInvalidaException, GerarPlanilhaException, ElementoNaoEncontradoException, ProdutoNaoEncontradoException, TimerUtilException {
        SeleniumUtil.navegar(webDriver, link);

        logger.info(String.format("Processando pendências no %s...", nomeEcommerce));
        for (PendenciaConsultaProdutos pendenciaConsultaProdutos : pendenciasConsultaProdutos) {
            processarPendencia(webDriver, numeroResultados, pendenciaConsultaProdutos);
        }

        logger.info("Gerando planilha de resultados...");
        gerarPlanilhasResultado(planilhas, googleDrivePathResultados, pendenciasConsultaProdutos);
    }

    public abstract void processarPendencia(WebDriver webDriver, int numeroResultados, PendenciaConsultaProdutos pendenciaConsultaProdutos) throws ElementoNaoEncontradoException, ProdutoNaoEncontradoException, TimerUtilException;

    public void gerarPlanilhasResultado(List<Planilha> planilhas, String googleDrivePathResultados, List<PendenciaConsultaProdutos> pendenciasConsultaProdutos) throws GerarPlanilhaException {
        for (Planilha planilha : planilhas) {
            String nomePlanilhaGerada = googleDrivePathResultados +"\\" + planilha.getNome().replace(".xlsx", "").replace(".xls", "") + nomeEcommerce + ConversorUtil.getDateToString(Calendar.getInstance(), ConversorUtil.getDateToString(Calendar.getInstance(), "dd_MM_yyyy_HH_mm_sss")) + ".xlsx";
            GoogleDriveUtil.gerarPlanilha(new File(nomePlanilhaGerada), identificarInfosPlanilha(planilha.getNome(), pendenciasConsultaProdutos));
        }
    }

    private List<List<String>> identificarInfosPlanilha(String nomePlanilha, List<PendenciaConsultaProdutos> pendencias) {
        List<List<String>> retorno = new ArrayList<>();

        for (PendenciaConsultaProdutos pendencia : pendencias) {
            List<String> linha = new ArrayList<>();
            retorno.add(linha);
            if (pendencia.getPlanilha().getNome().equals(nomePlanilha)) {
                linha.add(pendencia.getNomeProduto());

                for (int i = 0; i < pendencia.getValores().size(); i++) {
                    linha.add(pendencia.getLinks().get(i) + " / " + pendencia.getValores().get(i));
                }
                pendencia.zerarInfosExtraidas();
            }
        }

        return retorno;
    }
}
