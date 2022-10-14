package br.com.convergencia.testejavar1.controller.handler;

public enum ExceptionTypeEnum {

    INVALID_STATUS(1, "invalid status."),
    CPF_ALREADY_REGISTERED(2, "cpf already registered");

    private final Integer id;

    private final String message;

    ExceptionTypeEnum(Integer id, String message) {
        this.id = id;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
