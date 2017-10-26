package sk.flowy.msbiexport.repository;

import com.opencsv.ResultSetHelperService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j
@Repository
public class DbRepository {

    private static final String GENERATION_CSV_TABLE_NAME = "generovanie_csv";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void createDbTableIfDoesntExists(){
        if (!checkIfTableExists()){
            jdbcTemplate.execute(DbQuery.createGenerationCsvTable(GENERATION_CSV_TABLE_NAME));
            jdbcTemplate.execute(DbQuery.populateGenerationCsvTableWithData());
        }
    }

    public List<String> getListOfTables() {
        String query = DbQuery.getListOfTables();
        log.info("Querying DB: " + query);
        List<String> listOfTables = jdbcTemplate.query(query, (resultSet, i) -> resultSet.getString("TABLE_NAME"));
        log.info("Count of tables read in flowy: " + listOfTables.size());
        return listOfTables;
    }

    public List<String[]> getAllFromTableForClient(String tableName, Integer clientId, Date lastGenerationTime) {
        ResultSetHelperService resultSetHelperService = new ResultSetHelperService();
        String query = DbQuery.getAllDataForClient(tableName, clientId, lastGenerationTime);

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
                log.error("Problem with getting column values from resultSet. ", e);
            }
        });
        return data;
    }

    public void updateTimestampForClient(Integer clientId) {
        jdbcTemplate.update(DbQuery.updateTimestampQuery(clientId));
    }

    public Date getTimestampForClient(Integer clientId) {
        return jdbcTemplate.queryForObject(DbQuery.getDateForClientQuery(clientId), Date.class);
    }

    private boolean checkIfTableExists() {
        return jdbcTemplate.query(DbQuery.checkIfTableExistsQuery(GENERATION_CSV_TABLE_NAME), ResultSet::next);
    }
}

