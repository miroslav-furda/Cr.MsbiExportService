package sk.flowy.msbiexport.services;

import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipOutputStream;

public interface ZipCreationService {

    void zipData(ZipOutputStream zipOutputStream, Stream<Path> files);
}
