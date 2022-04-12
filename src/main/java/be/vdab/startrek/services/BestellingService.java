package be.vdab.startrek.services;

import be.vdab.startrek.domain.Bestelling;

import java.util.List;

public interface BestellingService {
    //findByWerknemerId is beter en korter
    List<Bestelling> findBestellingenByWerknemerId(long id);
//    List<Bestelling> findBestellingenByWerknemerIdAndLock(long id);

    //bestel() is beter en korter
    long createBestellingEnWijzigBudget(Bestelling bestelling);
}
