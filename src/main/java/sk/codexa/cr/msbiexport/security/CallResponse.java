package sk.codexa.cr.msbiexport.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class CallResponse {
    private String success;
    private String error;

    boolean hasError() {
        return error != null;
    }
}
