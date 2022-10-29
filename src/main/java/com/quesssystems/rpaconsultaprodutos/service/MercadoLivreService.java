package com.quesssystems.rpaconsultaprodutos.service;

import com.quesssystems.rpaconsultaprodutos.automacao.PendenciaConsultaProdutos;
import com.quesssystems.rpaconsultaprodutos.exceptions.ProdutoNaoEncontradoException;
import enums.UnidadesMedidaTempoEnum;
import exceptions.ElementoNaoEncontradoException;
import exceptions.TimerUtilException;
import org.openqa.selenium.*;
import org.springframework.stereotype.Service;
import util.SeleniumUtil;
import util.TimerUtil;

import java.util.List;

@Service
public class MercadoLivreService extends EcommerceService {
    public MercadoLivreService() {
        super("_MERCADOLIVRE_", "https://www.mercadolivre.com.br/");
    }

    @Override
    public void processarPendencia(WebDriver webDriver, int numeroResultados, PendenciaConsultaProdutos pendenciaConsultaProdutos) throws ElementoNaoEncontradoException, ProdutoNaoEncontradoException, TimerUtilException {
        pesquisarProduto(webDriver, pendenciaConsultaProdutos);

        while (true) {
            for (WebElement resultado : identificarProdutos(webDriver)) {
                if (pendenciaConsultaProdutos.getLinks().size() >= numeroResultados) {
                    break;
                }
                extrairInfos(resultado, pendenciaConsultaProdutos);
            }

            if (pendenciaConsultaProdutos.getLinks().size() >= numeroResultados) {
                break;
            }

            try {
                passarPagina(webDriver);
            }
            catch (ElementoNaoEncontradoException e) {
                break;
            }
        }
    }

    private void pesquisarProduto(WebDriver webDriver, PendenciaConsultaProdutos pendenciaConsultaProdutos) throws ElementoNaoEncontradoException, ProdutoNaoEncontradoException, TimerUtilException {
        SeleniumUtil.aguardarElementoVisivel(webDriver, 10, By.xpath("//input[@id='cb1-edit']")).clear();
        SeleniumUtil.aguardarElementoVisivel(webDriver, 10, By.xpath("//input[@id='cb1-edit']")).sendKeys(pendenciaConsultaProdutos.getNomeProduto() + Keys.ENTER);

        try {
            SeleniumUtil.aguardarElementoVisivel(webDriver, 10, By.xpath("//span[contains(text(), 'resultado')]"));
        }
        catch (ElementoNaoEncontradoException e) {
            throw new ProdutoNaoEncontradoException(pendenciaConsultaProdutos.getNomeProduto());
        }

        ordenarResultados(webDriver);
    }

    private void ordenarResultados(WebDriver webDriver) throws ElementoNaoEncontradoException, TimerUtilException {
        SeleniumUtil.aguardarElementoClicavel(webDriver, 10, By.xpath("//button[@type='button']")).click();
        TimerUtil.aguardar(UnidadesMedidaTempoEnum.SEGUNDOS, 2);
        SeleniumUtil.aguardarElementoClicavel(webDriver, 10, By.xpath("//li[@data-key='price_asc']")).click();
        TimerUtil.aguardar(UnidadesMedidaTempoEnum.SEGUNDOS, 3);
    }

    private List<WebElement> identificarProdutos(WebDriver webDriver) throws ElementoNaoEncontradoException {
        return SeleniumUtil.aguardarElementosVisiveis(webDriver, 10, By.xpath("//div[@class='ui-search-result__content-wrapper shops__result-content-wrapper']"));
    }

    private void passarPagina(WebDriver webDriver) throws ElementoNaoEncontradoException {
        try {
            SeleniumUtil.aguardarElementoClicavel(webDriver, 10, By.xpath("//a[@title='Seguinte']")).click();
        }
        catch (ElementClickInterceptedException e) {
            SeleniumUtil.aguardarElementoClicavel(webDriver, 10, By.xpath("//button[contains(text(), 'Entendi')]")).click();
            SeleniumUtil.aguardarElementoClicavel(webDriver, 10, By.xpath("//a[@title='Seguinte']")).click();
        }
    }

    private void extrairInfos(WebElement resultado, PendenciaConsultaProdutos pendenciaConsultaProdutos) {
        pendenciaConsultaProdutos.addLink(resultado.findElement(By.xpath("..")).getAttribute("href"));
        String xpathValor = "div[3]/div/div/div/span/span[2]/span[2]";
        String xpathValorDecimal = "div[3]/div/div/div/span/span[2]/span[4]";
        String valor;
        String valorDecimal = ",";
        try {
            resultado.findElement(By.xpath("div/div/label"));
        }
        catch (NoSuchElementException e) {
            xpathValor = "div[2]/div/div/div/span[1]/span[2]/span[2]";
            xpathValorDecimal = "div[2]/div/div/div/span[1]/span[2]/span[4]";
        }

        valor = resultado.findElement(By.xpath(xpathValor)).getText();
        try {
            valorDecimal = valorDecimal + resultado.findElement(By.xpath(xpathValorDecimal)).getText();
        }
        catch (NoSuchElementException e){
            valorDecimal = valorDecimal + "00";
        }
        valor = valor + valorDecimal;
        pendenciaConsultaProdutos.addValor(valor);
    }
}
