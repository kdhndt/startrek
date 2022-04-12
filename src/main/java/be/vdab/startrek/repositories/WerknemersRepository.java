package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Werknemer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WerknemersRepository {
    List<Werknemer> findAll();
    Optional<Werknemer> findById(long id);
//    Optional<Werknemer> findByIdAndLock(long id);
    void verlaagBudget(long id, BigDecimal bedrag);
}
