package sk.flowy.msbiexport.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

//TODO remove test
@RestController
public class UserController {

    @RequestMapping(value = "/me", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> authenticatedUser() {
        Response response = new Response();
        response.setSuccess("true");

        return new ResponseEntity<>(response, OK);
    }
}
