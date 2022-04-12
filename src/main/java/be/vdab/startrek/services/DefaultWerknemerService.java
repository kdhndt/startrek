package be.vdab.startrek.services;

import be.vdab.startrek.domain.Werknemer;
import be.vdab.startrek.repositories.WerknemersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DefaultWerknemerService implements WerknemerService {

    private final WerknemersRepository werknemersRepository;

    public DefaultWerknemerService(WerknemersRepository repository) {
        this.werknemersRepository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Werknemer> findAll() {
        return werknemersRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Werknemer> findById(long id) {
        return werknemersRepository.findById(id);
    }

/*    @Override
    public Optional<Werknemer> findByIdAndLock(long id) {
        return werknemersRepository.findByIdAndLock(id);
    }*/

/*    @Override
    public void wijzigBudget(long id, BigDecimal bedrag) {
        werknemersRepository.verlaagBudget(id, bedrag);
    }*/
}
