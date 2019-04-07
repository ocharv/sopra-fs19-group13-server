//@Author Fabian Küffer 15-931-421

package ch.uzh.ifi.seal.soprafs19.Exception;

public class UserDoesNotExistException extends RuntimeException {
    //used when a query searches for an user that does not exist
    public UserDoesNotExistException(){
        super("User not found. Check input");
        System.out.println("login failed");
    }

}
