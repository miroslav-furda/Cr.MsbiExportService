package sk.flowy.msbiexport.db;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class DbCaller {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbQuery dbQuery;

    public void getAllAttributes() {
        String query = dbQuery.getAdminsTableColumnCount();
        log.info("Querying DB: " + query);
        Integer countOfColumns = jdbcTemplate.queryForObject(query,Integer.class);
        log.info("Count of columns in admins table: " + countOfColumns);
    }
}

