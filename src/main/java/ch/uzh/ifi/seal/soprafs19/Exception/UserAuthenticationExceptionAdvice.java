//@Author Fabian Küffer 15-931-421

package ch.uzh.ifi.seal.soprafs19.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserAuthenticationExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(UserAuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String userAuthenticationExceptionHandler(UserAuthenticationException exception) {
        //handler for when an authentication failed
        System.out.println("Auth failed");
        return exception.getMessage();
    }

}
