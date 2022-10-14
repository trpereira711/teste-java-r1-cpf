package br.com.convergencia.testejavar1.repository;

import br.com.convergencia.testejavar1.model.CpfModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CpfRepository extends JpaRepository<CpfModel, Long> {
    Optional<CpfModel> findByNumber(String number);
}
