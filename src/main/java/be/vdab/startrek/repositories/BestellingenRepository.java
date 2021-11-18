package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Bestelling;

import java.util.List;

public interface BestellingenRepository {
    List<Bestelling> findBestellingenByWerknemerId(long id);
    List<Bestelling> findBestellingenByWerknemerIdAndLock(long id);
    long create(Bestelling bestelling);
}
