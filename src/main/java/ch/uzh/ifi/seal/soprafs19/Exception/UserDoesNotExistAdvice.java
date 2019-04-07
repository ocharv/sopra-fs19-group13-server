//@Author Fabian Küffer 15-931-421

package ch.uzh.ifi.seal.soprafs19.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class UserDoesNotExistAdvice{

    @ResponseBody
    @ExceptionHandler(UserDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userDoesNotExistHandler(UserDoesNotExistException ex){
        //handles when an user does not exist and throws http status.notfound
        System.out.println("user does not exist handler");
        return ex.getMessage();
        }
}

