package br.com.convergencia.testejavar1.config.exception;

import br.com.convergencia.testejavar1.controller.handler.ExceptionTypeEnum;

public class NegocioException extends RuntimeException {

    private final ExceptionTypeEnum type;

    public NegocioException(ExceptionTypeEnum type) {
        super("");
        this.type = type;
    }

    public NegocioException(ExceptionTypeEnum type, String message) {
        super(message);
        this.type = type;
    }

    public ExceptionTypeEnum getType() {
        return type;
    }
}
