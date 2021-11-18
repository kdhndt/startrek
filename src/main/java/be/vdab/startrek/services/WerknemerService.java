package be.vdab.startrek.services;

import be.vdab.startrek.domain.Werknemer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WerknemerService {
    List<Werknemer> findAll();
    Optional<Werknemer> findById(long id);
    Optional<Werknemer> findByIdAndLock(long id);
    void wijzigBudget(long id, BigDecimal bedrag);
}
