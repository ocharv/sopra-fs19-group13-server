//@Author Fabian Küffer 15-931-421

package ch.uzh.ifi.seal.soprafs19.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserDuplicateAdvice {
    @ResponseBody
    @ExceptionHandler(UserDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String userDuplicateHandler(UserDuplicateException ex){
        //handler for when changing username or registering an user and there is already an user
        //with the same name in the database
        System.out.println("dupl advice");
        return ex.getMessage();
    }
}
