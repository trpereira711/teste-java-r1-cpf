package br.com.convergencia.testejavar1.controller.form;

import br.com.convergencia.testejavar1.model.CpfModel;
import org.hibernate.validator.constraints.br.CPF;
import javax.validation.constraints.NotBlank;

public record CpfForm(@NotBlank @CPF String number) {

    public CpfModel convert() {
        return new CpfModel(this.number.replaceAll("[.-]", ""));
    }
}
