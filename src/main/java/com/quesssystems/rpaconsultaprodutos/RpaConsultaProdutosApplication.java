package com.quesssystems.rpaconsultaprodutos;

import com.quesssystems.rpaconsultaprodutos.service.RpaService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class RpaConsultaProdutosApplication {
	private final RpaService rpaService;

	public RpaConsultaProdutosApplication(RpaService rpaService) {
		this.rpaService = rpaService;
	}

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(RpaConsultaProdutosApplication.class);
		builder.headless(false);
		builder.run(args);
	}

	@PostConstruct
	public void init() {
		rpaService.iniciarAutomacao();
	}
}
