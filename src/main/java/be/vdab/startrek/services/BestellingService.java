package be.vdab.startrek.services;

import be.vdab.startrek.domain.Bestelling;

import java.util.List;

public interface BestellingService {
    List<Bestelling> findBestellingenByWerknemerId(long id);
    List<Bestelling> findBestellingenByWerknemerIdAndLock(long id);
    long createBestellingEnWijzigBudget(Bestelling bestelling);
}
