package br.com.convergencia.testejavar1.controller.form;

import br.com.convergencia.testejavar1.model.enums.StatusEnum;
import javax.validation.constraints.NotNull;

public record UpdateCpfStatusForm(@NotNull StatusEnum status) {


}
