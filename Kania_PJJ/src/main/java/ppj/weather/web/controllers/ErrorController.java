package ppj.weather.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ppj.weather.Main;


@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private String bodyOfResponse = "Something is wrong.";

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        log.error("Exception controller: ", ex);

        return handleExceptionInternal(ex, bodyOfResponse + "\n" + ex.getMessage(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
