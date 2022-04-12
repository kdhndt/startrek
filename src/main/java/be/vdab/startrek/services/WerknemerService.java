package be.vdab.startrek.services;

import be.vdab.startrek.domain.Werknemer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WerknemerService {
    List<Werknemer> findAll();
    Optional<Werknemer> findById(long id);
//    Optional<Werknemer> findByIdAndLock(long id);
    //wijzigBudget van onze repo wordt hier niet aangemaakt, want dit kan enkel gebeuren bij het aanmaken van een nieuwe record, dus in je BestellingenService
//    void wijzigBudget(long id, BigDecimal bedrag);
}
