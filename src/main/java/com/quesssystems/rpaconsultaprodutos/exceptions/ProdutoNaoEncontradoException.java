package com.quesssystems.rpaconsultaprodutos.exceptions;

public class ProdutoNaoEncontradoException extends Exception {
    public ProdutoNaoEncontradoException(String nomeProduto) {
        super(String.format("Produto %s não encontrado", nomeProduto));
    }
}
