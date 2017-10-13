package sk.flowy.msbiexport;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        dbCaller.getAllAttributes();
        return null;
    }

}
