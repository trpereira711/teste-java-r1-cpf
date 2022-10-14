package br.com.convergencia.testejavar1.controller.handler;

import br.com.convergencia.testejavar1.config.exception.NegocioException;
import br.com.convergencia.testejavar1.controller.handler.dto.ExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ControllerAdvice
public class ExceptionRestControllerHandler {

    private static final String HANDLE_ERROR_TYPE_MESSAGE = "method=handle, error_type={}, message={}";

    private final Logger logger = LoggerFactory.getLogger(ExceptionRestControllerHandler.class);

    private final MessageSource messageSource;

    public ExceptionRestControllerHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Collection<ExceptionDto> handle(MethodArgumentNotValidException exception) {
        List<ExceptionDto> errors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e -> {
            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            errors.add(new ExceptionDto(0, message, e.getField()));
        });

        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDto> handle(HttpMessageNotReadableException e) {
        logger.error(HANDLE_ERROR_TYPE_MESSAGE, ExceptionTypeEnum.INVALID_STATUS, ExceptionTypeEnum.INVALID_STATUS.getMessage(), e);
        return transcode(HttpStatus.BAD_REQUEST, ExceptionTypeEnum.INVALID_STATUS, e);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ExceptionDto> handle(NegocioException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(2, e.getType().getMessage(), e.getMessage()));
    }

    private ResponseEntity<ExceptionDto> transcode(HttpStatus status, ExceptionTypeEnum type, Throwable throwable) {
        return ResponseEntity.status(status)
                .body(new ExceptionDto(type.getId(), type.getMessage(), throwable.getMessage()));
    }
}
