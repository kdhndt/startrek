package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Werknemer;
import be.vdab.startrek.exceptions.OnvoldoendeBudgetException;
import be.vdab.startrek.exceptions.WerknemerNietGevondenException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import(JdbcWerknemersRepository.class)
@Sql("/insertWerknemers.sql")
class JdbcWerknemersRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String WERKNEMERS = "werknemers";
    private final JdbcWerknemersRepository repository;

    JdbcWerknemersRepositoryTest(JdbcWerknemersRepository repository) {
        this.repository = repository;
    }

    private long idVanTestWerknemer1() {
        return super.jdbcTemplate.queryForObject(
                "select id from werknemers where voornaam='test1Voornaam'", Long.class);
    }

    @Test
    void findAll() {
        assertThat(repository.findAll())
                .hasSize(countRowsInTable(WERKNEMERS))
                //sortering controleren
                .extracting(Werknemer::getVoornaam)
                .isSortedAccordingTo(String::compareToIgnoreCase);
    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestWerknemer1()))
                .hasValueSatisfying(werknemer -> assertThat(werknemer.getNaam()).isEqualTo("test1voornaam test1familienaam"));
    }

    @Test
    void findByOnbestaandeIdVindtGeenWerknemer() {
        assertThat(repository.findById(-1)).isEmpty();
    }

    @Test
    void verlaagBudgetOnbestaandeWerknemer() {
        assertThatExceptionOfType(WerknemerNietGevondenException.class)
                .isThrownBy(() -> repository.verlaagBudget(-1, BigDecimal.TEN));
    }

    @Test
    void verlaagVoldoendeBudget() {
        //geen assert nodig, maak gewoon dat je geen errors krijgt
        repository.verlaagBudget(idVanTestWerknemer1(), BigDecimal.valueOf(5));
    }

    @Test
    void verlaagOnvoldoendeBudget() {
        assertThatExceptionOfType(OnvoldoendeBudgetException.class)
                .isThrownBy(() -> repository.verlaagBudget(idVanTestWerknemer1(), BigDecimal.valueOf(2000)));
    }

}