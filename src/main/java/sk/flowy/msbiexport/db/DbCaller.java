package sk.flowy.msbiexport.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DbCaller {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbQuery dbQuery;

    public void getAllAttributes() {
        String query = dbQuery.getAdminsTableColumnCount();
        jdbcTemplate.queryForObject(query,Integer.class);
    }
}

