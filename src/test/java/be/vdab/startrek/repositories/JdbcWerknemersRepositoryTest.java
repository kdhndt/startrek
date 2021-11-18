package be.vdab.startrek.repositories;

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
                .hasSize(countRowsInTable(WERKNEMERS));
    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestWerknemer1()))
                .hasValueSatisfying(werknemer -> assertThat(werknemer.getVoornaam()).isEqualTo("test1voornaam"));
    }

    @Test
    void updateOnbestaandeWerknemer() {
        assertThatExceptionOfType(WerknemerNietGevondenException.class)
                .isThrownBy(() -> repository.wijzigBudget(15, BigDecimal.TEN));
    }

    @Test
    void updateOnvoldoendeBudget() {
        assertThatExceptionOfType(OnvoldoendeBudgetException.class)
                .isThrownBy(() -> repository.wijzigBudget(idVanTestWerknemer1(), BigDecimal.valueOf(2000)));
    }
}