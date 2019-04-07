//@Author Fabian Küffer 15-931-421

package ch.uzh.ifi.seal.soprafs19.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserDuplicateException extends RuntimeException {

    public UserDuplicateException(String name){
        //thrown when two users have the same username
        super("Error: reason <add User failed because username already exists>");

    }
}
