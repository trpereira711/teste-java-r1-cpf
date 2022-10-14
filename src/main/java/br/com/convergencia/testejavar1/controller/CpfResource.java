package br.com.convergencia.testejavar1.controller;

import br.com.convergencia.testejavar1.config.exception.NegocioException;
import br.com.convergencia.testejavar1.controller.form.UpdateCpfStatusForm;
import br.com.convergencia.testejavar1.controller.form.CpfForm;
import br.com.convergencia.testejavar1.controller.handler.ExceptionTypeEnum;
import br.com.convergencia.testejavar1.model.CpfModel;
import br.com.convergencia.testejavar1.model.dto.CpfDto;
import br.com.convergencia.testejavar1.repository.CpfRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;


@RestController
@RequestMapping("/cpfs")
public class CpfResource {

    private final Logger logger = LoggerFactory.getLogger(CpfResource.class);

    private final CpfRepository repository;

    public CpfResource(CpfRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CpfDto> registerNewCpf(@RequestBody @Valid CpfForm form, UriComponentsBuilder uriBuilder) {

        CpfModel cpfModel = form.convert();

        if (repository.findByNumber(cpfModel.getNumber()).isPresent()) {
            logger.info("method=registerNewCpf, message=cpf {} already registered", cpfModel.getNumber());
            throw new NegocioException(ExceptionTypeEnum.CPF_ALREADY_REGISTERED, "you already have a registered cpf");
        }

        CpfModel savedCpf = repository.save(cpfModel);
        logger.info("method=registerNewCpf, message=cpf {} registered successfully", cpfModel.getNumber());

        URI uri = uriBuilder.path("/cpfs/{cpf}").build(savedCpf.getNumber());
        return ResponseEntity.created(uri).body(new CpfDto(cpfModel.getNumber(), cpfModel.getStatus().name()));

    }

    @GetMapping("/{cpf}")
    public ResponseEntity<CpfDto> getByCpf(@PathVariable String cpf) {
        return repository.findByNumber(cpf.replaceAll("[.-]", ""))
                .map(c -> {
                    logger.info("method=getByCpf, message=cpf {} found successfully", c.getNumber());
                    return ResponseEntity.ok(new CpfDto(c.getNumber(), c.getStatus().name()));
                }).orElse(ResponseEntity.notFound().build());
    }


    @Transactional
    @PatchMapping(value = "/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CpfDto> updateCpfStatus(@PathVariable String cpf, @RequestBody @Valid UpdateCpfStatusForm form) {
        Optional<CpfModel> optional = repository.findByNumber(cpf.replaceAll("[.-]", ""));

        if (optional.isPresent()) {
            CpfModel foundCpf = optional.get();
            foundCpf.setStatus(form.status());

            logger.info("method=updateCpfStatus, message=cpf {} status {} update successfully", foundCpf.getNumber(), foundCpf.getStatus());
            return ResponseEntity.ok(new CpfDto(foundCpf.getNumber(), foundCpf.getStatus().name()));
        }

        return ResponseEntity.notFound().build();
    }
}
