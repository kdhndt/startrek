package be.vdab.startrek.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class WerknemerTest {

    private Werknemer werknemer;

    @BeforeEach
    void beforeEach() {
        werknemer = new Werknemer(1, "Kasper", "Dehondt", BigDecimal.valueOf(2000));
    }

    @Test
    void nieuwBudgetIs1000EUR() {
        werknemer.setBudget(BigDecimal.valueOf(1000));
        assertThat(werknemer.getBudget()).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    void nieuwBudgetKanNietNegatiefZijn() {
        assertThatIllegalArgumentException().isThrownBy(() -> werknemer.setBudget(BigDecimal.valueOf(-500)));
    }

}