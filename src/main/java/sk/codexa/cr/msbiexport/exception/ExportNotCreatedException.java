package sk.codexa.cr.msbiexport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Export creation failed.")
public class ExportNotCreatedException extends RuntimeException {
}
