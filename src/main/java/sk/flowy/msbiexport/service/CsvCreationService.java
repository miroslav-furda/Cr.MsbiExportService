package sk.flowy.msbiexport.service;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface CsvCreationService {

    Stream<Path> exportDataForMSBI(Integer klienID);
}
