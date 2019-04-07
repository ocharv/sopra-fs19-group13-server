//@Author Fabian Küffer 15-931-421

package ch.uzh.ifi.seal.soprafs19.Exception;


//https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
//Spring blog recommends handling exception not in controller and to write handlers (advice)



public class UserAuthenticationException extends RuntimeException {
    //Exception that when an authentication failed gets thrown
    public UserAuthenticationException() {

        super("Authentication failed");
        System.out.println("authentication failed");
    }

}