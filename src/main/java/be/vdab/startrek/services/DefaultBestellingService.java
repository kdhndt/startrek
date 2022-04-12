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

/*    @Override
    public List<Bestelling> findBestellingenByWerknemerIdAndLock(long id) {
        return bestellingenRepository.findBestellingenByWerknemerIdAndLock(id);
    }*/

    @Override
    public long createBestellingEnWijzigBudget(Bestelling bestelling) {
        //findByIdAndLock en zelfs findById zijn hier helemaal niet nodig.

        //voer samen uit in een transactie
        //wijzig werknemer record en lock het tot einde v/d transactie
        //zo kunnen er geen gelijktijdige problemen opduiken
        werknemersRepository.verlaagBudget(bestelling.getWerknemerId(), bestelling.getBedrag());
        return bestellingenRepository.create(bestelling);
    }
}
