package sk.flowy.msbiexport;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sk.flowy.msbiexport.db.DbCaller;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Log4j
@RestController
public class RestHandler {

    @Autowired
    private DbCaller dbCaller;

    @RequestMapping(
            value = "/flowy",
            method = GET)
    public @ResponseBody ResponseEntity<String> getAttributes() {

        log.info("I am getting all attributes ");
        dbCaller.getListOfTables();
        return null;
    }

    @RequestMapping(
            value = "/flowy/export/MSBI",
            produces = "application/zip",
            method = GET)
    public @ResponseBody void createExportForMSBI(HttpServletResponse response) {

        log.info("I am getting all attributes ");

        String filename = "MSBI_EXPORT_" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString() + ".zip";

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        zipData(zipOutputStream);

    }

    @RequestMapping(
            value = "/flowy/table/{table}/data",
            method = GET)
    public @ResponseBody ResponseEntity<String> getTableData(
            @PathVariable String table
    ) {
        log.info("Getting all data for: " + table);
        dbCaller.getAllFromTable(table, "");
        return null;
    }

    private void zipData(ZipOutputStream zipOutputStream) {
        Stream<Path> files = dbCaller.exportDataForMSBI();
        ZipOutputStream finalZipOutputStream = zipOutputStream;
        files.forEach(filePath -> {
            try {
                finalZipOutputStream.putNextEntry(new ZipEntry(filePath.toFile().getName()));
                FileInputStream fileInputStream = new FileInputStream(filePath.toFile());

                IOUtils.copy(fileInputStream,finalZipOutputStream);
                fileInputStream.close();
                finalZipOutputStream.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {
            zipOutputStream.close();
            finalZipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dbCaller.deleteTempData();
        }
    }


}
