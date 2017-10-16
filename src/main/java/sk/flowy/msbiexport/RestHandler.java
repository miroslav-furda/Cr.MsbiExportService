package sk.flowy.msbiexport;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sk.flowy.msbiexport.db.DbCaller;

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
            method = GET)
    public @ResponseBody ResponseEntity<String> createExportForMSBI() {

        log.info("I am getting all attributes ");
        dbCaller.exportDataForMSBI();
        return null;
    }

    @RequestMapping(
            value = "/flowy/table/{table}/data",
            method = GET)
    public @ResponseBody ResponseEntity<String> getTableData(
            @PathVariable String table
    ) {
        log.info("Getting all data for: " + table);
        dbCaller.getAllFromTable(table);
        return null;
    }

}
