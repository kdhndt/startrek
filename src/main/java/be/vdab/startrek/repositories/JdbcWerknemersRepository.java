package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Werknemer;
import be.vdab.startrek.exceptions.OnvoldoendeBudgetException;
import be.vdab.startrek.exceptions.WerknemerNietGevondenException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcWerknemersRepository implements WerknemersRepository {

    private final JdbcTemplate template;

    public JdbcWerknemersRepository(JdbcTemplate template) {
        this.template = template;
    }

    private final RowMapper<Werknemer> werknemerRowMapper =
            (result, rowNum) -> new Werknemer(result.getLong("id"), result.getString("voornaam"),
                    result.getString("familienaam"), result.getBigDecimal("budget"));

    @Override
    public List<Werknemer> findAll() {
        var sql = """
                select id, voornaam, familienaam, budget
                from werknemers
                order by voornaam
                """;
        return template.query(sql, werknemerRowMapper);
    }

    @Override
    public Optional<Werknemer> findById(long id) {
        try {
            var sql = """
                    select id, voornaam, familienaam, budget
                    from werknemers
                    where id = ?
                    """;
            return Optional.of(template.queryForObject(sql, werknemerRowMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

/*    @Override
    public Optional<Werknemer> findByIdAndLock(long id) {
        try {
            var sql = """
                    select id, voornaam, familienaam, budget
                    from werknemers
                    where id = ?
                    for update
                    """;
            return Optional.of(template.queryForObject(sql, werknemerRowMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }*/

    @Override
    public void verlaagBudget(long id, BigDecimal bedrag) {
        //dit is de SQL versie van een if statement, where id = ? and budget >= ?
        //als het budget niet >= bedrag, zal de statement simpelweg niet uitgevoerd worden
        var sql = """
                update werknemers
                set budget = budget - ?
                where id = ? and budget >= ?
                """;
        //aantal aangepaste records controleren
        if (template.update(sql, bedrag, id, bedrag) == 0) {
            if (findById(id).isEmpty()) {
                throw new WerknemerNietGevondenException();
            } else {
                throw new OnvoldoendeBudgetException();
            }
        }
    }
}
