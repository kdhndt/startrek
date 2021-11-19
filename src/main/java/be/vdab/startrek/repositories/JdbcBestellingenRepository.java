package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Bestelling;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class JdbcBestellingenRepository implements BestellingenRepository {

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public JdbcBestellingenRepository(JdbcTemplate template) {
        this.template = template;
        insert = new SimpleJdbcInsert(template)
                .withTableName("bestellingen")
                //onderstaande lijn wordt niet toegevoegd in voorbeeldoplossing?
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Bestelling> bestellingRowMapper =
            (result, rowNum) -> new Bestelling(result.getLong("id"), result.getLong("werknemerId"),
                    result.getString("omschrijving"), result.getBigDecimal("bedrag"));

    @Override
    public List<Bestelling> findBestellingenByWerknemerId(long id) {
        var sql = """
                select id, werknemerid, omschrijving, bedrag
                from bestellingen
                where werknemerid = ?
                order by id
                """;
        return template.query(sql, bestellingRowMapper, id);
    }

    @Override
    public List<Bestelling> findBestellingenByWerknemerIdAndLock(long id) {
        var sql = """
                select id, werknemerid, omschrijving, bedrag
                from bestellingen
                where werknemerid = ?
                for update
                """;
        return template.query(sql, bestellingRowMapper, id);
    }

    @Override
    public long create(Bestelling bestelling) {
        return insert.executeAndReturnKey(
                Map.of("werknemerid", bestelling.getWerknemerId(),
                        "omschrijving", bestelling.getOmschrijving(),
                        "bedrag", bestelling.getBedrag())
        ).longValue();
    }
}
