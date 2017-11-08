package sk.codexa.cr.msbiexport.service;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface CsvCreationService {

    /**
     *
     * @param klienID id of client which wants exported data.
     * @return Stream of Paths to csv files, which have exported data.
     */
    Stream<Path> exportDataForMSBI(Integer klienID);
}
