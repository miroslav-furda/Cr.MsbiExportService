package sk.flowy.msbiexport.csv;

import com.opencsv.CSVWriter;
import lombok.Data;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Data
public class ExportFile {

    private String filePath;
    private CSVWriter csvWriter;
    private final String TEMP_CSV_DIR = "MSBI_EXPORT";

    public ExportFile(String filePath) {
        this.filePath = filePath;
        try {
            csvWriter = new CSVWriter(new FileWriter(filePath), ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLineToCsv(List<String[]> data) {
        try {
            csvWriter.writeAll(data,Boolean.TRUE);
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
