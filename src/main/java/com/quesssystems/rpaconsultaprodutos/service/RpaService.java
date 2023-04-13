package com.quesssystems.rpaconsultaprodutos.service;

import automacao.AutomacaoApi;
import automacao.Planilha;
import automacao.Requisicao;
import com.quesssystems.rpaconsultaprodutos.automacao.PendenciaConsultaProdutos;
import com.quesssystems.rpaconsultaprodutos.automacao.PendenciaUtil;
import com.quesssystems.rpaconsultaprodutos.exceptions.ProdutoNaoEncontradoException;
import enums.NavegadoresEnum;
import enums.UnidadesMedidaTempoEnum;
import exceptions.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import util.*;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class RpaService {
    private final Logger logger = LoggerFactory.getLogger(RpaService.class);

    @Value("${api.recuperar-dados.link}")
    private String linkRecuperarDados;

    @Value("${api.registrar-log.link}")
    private String linkRegistrarLog;

    @Value("${api.id-automacao}")
    private Integer idAutomacao;

    @Value("${google-drive.path.pendentes}")
    private String googleDrivePathPendentes;

    @Value("${google-drive.path.processados}")
    private String googleDrivePathProcessados;

    @Value("${google-drive.path.resultados}")
    private String googleDrivePathResultados;

    @Value("${rpa.token}")
    private String token;

    @Value("${rpa.intervalo-minutos}")
    private Integer intervaloMinutos;

    @Value("${rpa.webdriver.path}")
    private String webDriverPath;

    @Value("${rpa.numero-resultados}")
    private Integer numeroResultados;

    @Value("${rpa.mercado-livre.ativo}")
    private boolean mercadoLivreAtivo;

    private final NavegadoresEnum navegador = NavegadoresEnum.CHROME;

    private final PendenciaUtil pendenciaUtil;

    private final MercadoLivreService mercadoLivreService;

    public RpaService(PendenciaUtil pendenciaUtil, MercadoLivreService mercadoLivreService) {
        this.pendenciaUtil = pendenciaUtil;
        this.mercadoLivreService = mercadoLivreService;
    }

    public void iniciarAutomacao() {
        logger.info("Iniciando automação...");

        try {
            while (true) {
                logger.info("Recuperando dados da automação...");
                AutomacaoApi automacaoApi = AutomacaoApiUtil.executarRequisicao(new Requisicao(linkRecuperarDados, token, idAutomacao, null));
                if (automacaoApi.isExecutar(Calendar.getInstance())) {
                    logger.info("Recuperando pendências...");
                    List<Planilha> planilhas = GoogleDriveUtil.recuperarPendencias(googleDrivePathPendentes, false);
                    List<PendenciaConsultaProdutos> pendenciasConsultaProdutos = new ArrayList<>();

                    if (planilhas.isEmpty()) {
                        logger.info("Sem planilhas pendentes");
                    } else {
                        logger.info("Convertendo planilhas em pendências...");
                        for (Planilha planilha : planilhas) {
                            pendenciasConsultaProdutos.addAll(pendenciaUtil.planilhaToPendencias(planilha));
                        }

                        if (!pendenciasConsultaProdutos.isEmpty()) {
                            logger.info("Acessando sites...");
                            WebDriver webDriver = WebdriverUtil.getWebDriver(navegador.toString(), webDriverPath);

                            if (mercadoLivreAtivo) {
                                mercadoLivreService.processarEcommerce(webDriver, numeroResultados, pendenciasConsultaProdutos, planilhas, googleDrivePathResultados);
                            }

                            logger.info("Fechando navegador...");
                            WebdriverUtil.fecharNavegador(webDriver);
                        }

                        logger.info("Movendo pendências...");
                        String nomePlanilhaProcessada = googleDrivePathProcessados + "\\" + ConversorUtil.getDateToString(Calendar.getInstance(), ConversorUtil.getDateToString(Calendar.getInstance(), "dd_MM_yyyy_HH_mm_sss")) + ".xlsx";
                        GoogleDriveUtil.moverPendencias(planilhas, new File(googleDrivePathPendentes), new File(nomePlanilhaProcessada));
                    }
                } else {
                    logger.info("Automação fora do período de execução");
                }

                logger.info("Registrando execução...");
                AutomacaoApiUtil.executarRequisicao(new Requisicao(linkRegistrarLog, token, idAutomacao, "Automação finalizada"));
                logger.info(String.format("Aguardando intervalo de %d minutos", intervaloMinutos));
                TimerUtil.aguardar(UnidadesMedidaTempoEnum.MINUTOS, intervaloMinutos);
            }
        }
        catch (RecuperarDadosException | AutomacaoNaoIdentificadaException | TimerUtilException | ArquivoException |
               MoverPendenciaException | NavegadorNaoIdentificadoException | DriverException | FecharNavegadorException |
               UrlInvalidaException | ElementoNaoEncontradoException | ProdutoNaoEncontradoException | GerarPlanilhaException |
               TokenInvalidoException | MensagemInvalidaException | RequisicaoException e) {
            if (!e.getClass().equals(AutomacaoNaoIdentificadaException.class) && !e.getClass().equals(TokenInvalidoException.class)) {
                try {
                    AutomacaoApiUtil.executarRequisicao(new Requisicao(linkRegistrarLog, token, idAutomacao, e.getMessage()));
                }
                catch (RecuperarDadosException | AutomacaoNaoIdentificadaException | RequisicaoException | TokenInvalidoException | MensagemInvalidaException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
