package sk.flowy.msbiexport.service;

import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipOutputStream;

public interface ZipCreationService {

    void zipData(ZipOutputStream zipOutputStream, Stream<Path> files);
}
