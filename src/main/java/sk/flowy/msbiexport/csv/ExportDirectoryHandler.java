package sk.flowy.msbiexport.csv;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipOutputStream;

@Data
@Component
public class ExportDirectoryHandler {

    private Path tempDirPath;
    private ZipOutputStream zipOutputStream;

    public ExportDirectoryHandler() {
        try {
            this.tempDirPath = Files.createTempDirectory("MSBI_EXPORT");
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempDirPath.toFile().deleteOnExit();
    }

}
