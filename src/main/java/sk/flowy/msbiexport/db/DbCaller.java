package sk.flowy.msbiexport.db;

import com.opencsv.ResultSetHelperService;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import sk.flowy.msbiexport.csv.ExportDirectoryHandler;
import sk.flowy.msbiexport.csv.ExportFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Log4j
@Component
public class DbCaller {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DbQuery dbQuery;

    @Getter
    @Autowired
    private ExportDirectoryHandler exportDirectoryHandler;

    private ExportFile exportFile;

    private ResultSetHelperService resultSetHelperService = new ResultSetHelperService();

    private List<String> listOfTables;

    public void getListOfTables() {
        String query = dbQuery.getListOfTables();
        log.info("Querying DB: " + query);
        listOfTables = jdbcTemplate.query(query, (resultSet, i) -> {
            String result = resultSet.getString("TABLE_NAME");
            return result;
        });
        log.info("Count of tables read in flowy: " + listOfTables.size());
    }

    public void getAllFromTable(String tableName, String tempDirPath) {
        String query = dbQuery.getAlldata(tableName);
        exportFile = new ExportFile(tempDirPath + "/" +tableName);
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
                e.printStackTrace();
            }
        });
        exportFile.writeLineToCsv(data);
    }

    public Stream<Path> exportDataForMSBI() {
        Stream<Path> files = null;
        getListOfTables();
        log.info("Temp files are in: " + exportDirectoryHandler.getTempDirPath().toString());
        for (String tableName : listOfTables
             ) {
            getAllFromTable(tableName, exportDirectoryHandler.getTempDirPath().toString());
        }
        try {
            files = Files.list(exportDirectoryHandler.getTempDirPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public void deleteTempData() {
        try {
            Files.list(exportDirectoryHandler.getTempDirPath()).forEach(file -> {
                file.toFile().delete();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Deleting temporary csv files");
    }

}

