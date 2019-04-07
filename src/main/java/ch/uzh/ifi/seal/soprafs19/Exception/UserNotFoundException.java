//@Author Fabian Küffer 15-931-421

package ch.uzh.ifi.seal.soprafs19.Exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        //Exception for when a query does not find an user
        super("User with userid: " + id +" was not found..");
        System.out.println("user not found");

    }
}
