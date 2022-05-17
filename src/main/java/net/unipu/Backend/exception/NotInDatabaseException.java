package net.unipu.Backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotInDatabaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NotInDatabaseException(String what, String where) {
        super(String.format("%s not found in %s", what, where));
    }
}