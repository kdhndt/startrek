package be.vdab.startrek.services;

import be.vdab.startrek.domain.Bestelling;
import be.vdab.startrek.repositories.BestellingenRepository;
import be.vdab.startrek.repositories.WerknemersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DefaultBestellingService implements BestellingService {

    private final BestellingenRepository bestellingenRepository;
    private final WerknemersRepository werknemersRepository;

    public DefaultBestellingService(BestellingenRepository repository, WerknemersRepository werknemersRepository) {
        this.bestellingenRepository = repository;
        this.werknemersRepository = werknemersRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Bestelling> findBestellingenByWerknemerId(long id) {
        return bestellingenRepository.findBestellingenByWerknemerId(id);
    }

    @Override
    public List<Bestelling> findBestellingenByWerknemerIdAndLock(long id) {
        return bestellingenRepository.findBestellingenByWerknemerIdAndLock(id);
    }

    @Override
    public long createBestellingEnWijzigBudget(Bestelling bestelling) {
        //lock beide voor het wijzigen
        bestellingenRepository.findBestellingenByWerknemerIdAndLock(bestelling.getId());
        werknemersRepository.findByIdAndLock(bestelling.getId());
        //voer samen uit in een transactie
        werknemersRepository.wijzigBudget(bestelling.getWerknemerId(), bestelling.getBedrag());
        return bestellingenRepository.create(bestelling);
    }
}
