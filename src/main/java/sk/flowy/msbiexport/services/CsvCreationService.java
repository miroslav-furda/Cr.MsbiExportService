package sk.flowy.msbiexport.services;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface CsvCreationService {

    void writeLineToCsv(List<String[]> data, String filePath);

    Stream<Path> exportDataForMSBI();
}
