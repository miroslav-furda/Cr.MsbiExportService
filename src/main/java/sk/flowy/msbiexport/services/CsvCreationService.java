package sk.flowy.msbiexport.services;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface CsvCreationService {

    Stream<Path> exportDataForMSBI(Integer klienID);
}
