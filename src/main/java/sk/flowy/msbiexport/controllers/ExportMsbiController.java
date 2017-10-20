package sk.flowy.msbiexport.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.flowy.msbiexport.exception.ExportNotCreatedException;
import sk.flowy.msbiexport.services.CsvCreationService;
import sk.flowy.msbiexport.services.ZipCreationService;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipOutputStream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Log4j
@RestController
@RequestMapping("/api/flowy")
public class ExportMsbiController {

    @Autowired
    private CsvCreationService csvCreationService;

    @Autowired
    private ZipCreationService zipCreationService;

    @RequestMapping(
            value = "/export/MSBI",
            produces = "application/zip",
            method = GET)
    public void createExportForMSBI(HttpServletResponse response) {

        log.info("Getting all attributes ");

        String filename = "MSBI_EXPORT_" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".zip";

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(response.getOutputStream());
        } catch (IOException e) {
            log.error("Export creation failed");
            throw new ExportNotCreatedException();
        }
        zipCreationService.zipData(zipOutputStream, csvCreationService.exportDataForMSBI());
    }
}
