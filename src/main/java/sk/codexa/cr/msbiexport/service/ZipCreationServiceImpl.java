package sk.codexa.cr.msbiexport.service;

import lombok.extern.log4j.Log4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Log4j
public class ZipCreationServiceImpl implements ZipCreationService{

    @Override
    public void zipData(ZipOutputStream zipOutputStream, Stream<Path> files) {
        files.forEach(filePath -> writeZip(filePath, zipOutputStream));
        try {
            zipOutputStream.close();
        } catch (IOException e) {
            log.error("Closing Streams was not successful");
            e.printStackTrace();
        }
    }

    private void writeZip(Path filePath, ZipOutputStream zipOutputStream) {
        try (
                FileInputStream fileInputStream = new FileInputStream(filePath.toFile())
        ) {
            zipOutputStream.putNextEntry(new ZipEntry(filePath.toFile().getName()));
            IOUtils.copy(fileInputStream, zipOutputStream);
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            log.error("IOException caught, there can be a problem either with writing to ZIP file or with copying one input stream to another");
            e.printStackTrace();
        }
        try {
            log.info("Deleting file: " + filePath.toString());
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
