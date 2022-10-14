package br.com.convergencia.testejavar1.model;

import br.com.convergencia.testejavar1.model.enums.StatusEnum;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "tb_cpfs")
public class CpfModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Deprecated
    public CpfModel() {
    }

    public CpfModel(String value) {
        this.number = value;
        this.status = StatusEnum.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CpfModel cpf = (CpfModel) o;
        return number.equals(cpf.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

}
