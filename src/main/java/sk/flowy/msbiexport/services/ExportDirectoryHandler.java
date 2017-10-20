package sk.flowy.msbiexport.services;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipOutputStream;

@Data
@Component
@Log4j
public class ExportDirectoryHandler {

    private Path tempDirPath;
    private ZipOutputStream zipOutputStream;

    public ExportDirectoryHandler() {
        try {
            this.tempDirPath = Files.createTempDirectory("MSBI_EXPORT");
        } catch (IOException e) {
            log.error("Error creating temporary directory");
            e.printStackTrace();
        }
        tempDirPath.toFile().deleteOnExit();
    }

}
