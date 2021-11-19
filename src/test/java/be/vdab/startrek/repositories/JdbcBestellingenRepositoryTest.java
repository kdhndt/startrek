package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Bestelling;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Sql("/insertBestellingen.sql")
@Import(JdbcBestellingenRepository.class)
class JdbcBestellingenRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String BESTELLINGEN = "bestellingen";
    private final JdbcBestellingenRepository repository;

    JdbcBestellingenRepositoryTest(JdbcBestellingenRepository repository) {
        this.repository = repository;
    }

    private long werknemerIdVanTest() {
        //gebruik hier group by in je statement zodat er maar 1 resultaat getoond wordt
        return jdbcTemplate.queryForObject("select werknemerId from bestellingen where bedrag = 250 or bedrag = 75 group by werknemerId", Long.class);
    }

    @Test
    void findBestellingenByWerknemerId() {
        var werknemerId = werknemerIdVanTest();
        //dit gebruikt de where clausule uit je echte repository method
        assertThat(repository.findBestellingenByWerknemerId(werknemerId))
                //hier moet je zelf het where statement nog eens gaan definieren om correct te testen
                .hasSize(countRowsInTableWhere(BESTELLINGEN, "werknemerid=" + werknemerId))
                .allSatisfy(bestelling -> assertThat(bestelling.getWerknemerId()).isEqualTo(werknemerId))
                .extracting(Bestelling::getId)
                .isSorted();
    }

    @Test
    void findByOnbestaandeWerknemerGeeftEenLegeLijst() {
        assertThat(repository.findBestellingenByWerknemerId(-1L)).isEmpty();
    }

    @Test
    void create() {
        var bestellingId = repository.create(new Bestelling(0, 1, "testProduct", BigDecimal.TEN));
        assertThat(bestellingId).isPositive();
        assertThat(countRowsInTableWhere(BESTELLINGEN, "id = " + bestellingId)).isOne();
    }
}