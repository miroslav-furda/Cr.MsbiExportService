package sk.flowy.msbiexport.services;

import com.opencsv.CSVWriter;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.flowy.msbiexport.repository.DbRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Data
@Log4j
@Service
public class CsvCreationServiceImpl implements CsvCreationService{

    @Value("${tempPath}")
    private String tempDir;

    private final String TEMP_CSV_DIR = "MSBI_EXPORT";

    private DbRepository dbRepository;

    public CsvCreationServiceImpl(DbRepository dbRepository) {
        this.dbRepository = dbRepository;
    }


    private void writeLineToCsv(List<String[]> data, String filePath) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath), ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);){
            csvWriter.writeAll(data,Boolean.TRUE);
        } catch (IOException e) {
            log.error("Error closing csv writer");
            e.printStackTrace();
        }
    }

    @Override
    public Stream<Path> exportDataForMSBI(Integer klientID) {
        Stream<Path> files = null;
        List<String> listOfTables = dbRepository.getListOfTables();
        Path tempDirPath = null;
        try {
            tempDirPath = Files.createTempDirectory(tempDir);
            tempDirPath.toFile().deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Temp files are in: " + tempDirPath.toString());
        for (String tableName : listOfTables
                ) {
            writeLineToCsv(dbRepository.getAllFromTableForClient(tableName, klientID), tempDirPath + "/" +tableName);
        }
        try {
            files = Files.list(tempDirPath);
        } catch (IOException e) {
            log.error("Error retrieving stream");
            e.printStackTrace();
        }
        return files;
    }


}
