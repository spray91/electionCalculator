package pl.spray.electioncalculator.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalServerErrorException extends Exception{

    public ExternalServerErrorException(String message, Throwable cause){
        super(message);
        if(cause != null){
            this.initCause(cause);
        }
        log.error("Application must be turned off due to problem with external server. Message: " + message);
    }
}
