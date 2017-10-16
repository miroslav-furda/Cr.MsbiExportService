package sk.flowy.msbiexport.csv;

import com.opencsv.CSVWriter;
import lombok.Data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Data
public class ExportFile {

    private String filename;
    private CSVWriter csvWriter;

    public ExportFile(String tableName) {
        this.filename = "C:/dev/" + tableName + ".csv";
        try {
            csvWriter = new CSVWriter(new FileWriter(filename), ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
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
