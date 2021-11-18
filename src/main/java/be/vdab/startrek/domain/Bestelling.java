package be.vdab.startrek.domain;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Bestelling {
    //automatisch aangemaakt
    private final long id;
    //beschikbaar via PathVariable
    private final long werknemerId;
    //volgende 2 member variables worden door de bedienden ingevuld
    @NotBlank
    private final String omschrijving;
    @NotNull @Positive @NumberFormat(pattern = "#,##0.00")
    private final BigDecimal bedrag;

    public Bestelling(long id, long werknemerId, String omschrijving, BigDecimal bedrag) {
        this.id = id;
        this.werknemerId = werknemerId;
        this.omschrijving = omschrijving;
        this.bedrag = bedrag;
    }



    public long getId() {
        return id;
    }

    public long getWerknemerId() {
        return werknemerId;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public BigDecimal getBedrag() {
        return bedrag;
    }
}
