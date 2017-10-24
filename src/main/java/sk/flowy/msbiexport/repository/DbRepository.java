package sk.flowy.msbiexport.repository;

import com.opencsv.ResultSetHelperService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Repository
public class DbRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getListOfTables() {
        String query = DbQuery.getListOfTables();
        log.info("Querying DB: " + query);
        List<String> listOfTables = jdbcTemplate.query(query, (resultSet, i) -> {
            String result = resultSet.getString("TABLE_NAME");
            return result;
        });
        log.info("Count of tables read in flowy: " + listOfTables.size());
        return listOfTables;
    }

    public List<String[]> getAllFromTableForClient(String tableName, Integer klientID) {
        ResultSetHelperService resultSetHelperService = new ResultSetHelperService();
        String query = DbQuery.getAllDataForKlient(tableName, klientID);
        log.info("Query: " + query);
        List<String[]> data  = new ArrayList<>();
        final int[] iteration = {0};
        jdbcTemplate.query(query, resultSet -> {
            String[] row;
            if (iteration[0] == 0) {
                row = resultSetHelperService.getColumnNames(resultSet);
                data.add(row);
                iteration[0]++;
            }
            try {
                row = resultSetHelperService.getColumnValues(resultSet);
                data.add(row);
            } catch (IOException e) {
                log.error("Problem with getting column values from resultSet");
                e.printStackTrace();
            }
        });
        return data;
    }
}

