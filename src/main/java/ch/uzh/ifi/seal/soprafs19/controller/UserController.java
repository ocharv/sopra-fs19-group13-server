//@Author Fabian Küffer 15-931-421

package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Exception.*;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

@RestController
@Controller
public class UserController {


    @Autowired
    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @GetMapping("/users")
    ResponseEntity<Iterable<User>> all(
            @RequestHeader(value = "token") String token,
            HttpServletResponse response
    ) {
        System.out.println("\nGET /users");
        System.out.println("token: " + token);
        //returns all users
        return new ResponseEntity<Iterable<User>>(service.getUsers(token), HttpStatus.OK);

    }


    //REGISTER
    @PostMapping("/users")
    @ResponseBody
    ResponseEntity<String> createUser(@RequestBody User newUser) {
        //create a new user, used in registering
        System.out.println("\nPOST /users");

        if (service.userExists(newUser)) {
            throw new UserDuplicateException(newUser.getUsername());
        }
        //user does not yet exist, create user
        User currentUser = this.service.createUser(newUser);
        String str = "/users/" + currentUser.getId().toString();
        //return according to specifications the /users/userid and also the status 201
        System.out.print("created user with username: " + newUser.getUsername());
        return new ResponseEntity<String>(str, HttpStatus.CREATED);

    }





    // /USERS/

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users/{userId}")
    ResponseEntity<User> getId(@PathVariable long userId,
                               @RequestHeader(value = "token") String token,
                               HttpServletResponse response
    ) {
        //get user by their id
        System.out.println("\nGET /users/userid");
        System.out.println("Got token: " + token);
        if (service.authenticated(service.getUserById(userId), token)) {
            //user is authenticated
            try {
                return new ResponseEntity<>(this.service.getUserById(userId), HttpStatus.OK);
            } catch (Exception e) {
                //did not find user, throw usernotfound here
                System.out.println("catched nullpointer");
                throw new UserNotFoundException(userId);
            }
        } else {
            //user is not authenticated
            System.out.println("Auth failed");
            throw new UserAuthenticationException();
        }
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/users/{userId}")
    @ResponseBody
    public ResponseEntity<User> update(@RequestBody User user, @PathVariable long userId,
                                       @RequestHeader(value = "token") String token,
                                       HttpServletResponse response
    ) {
        //updates user
        System.out.println("\nPUT /users/userid");
        System.out.println("ID: " + user.getId());
        System.out.println("PathID: " + userId);
        System.out.println("Header Token: " + token);
        System.out.println("User Token: " + service.getUserbyToken(token).getId());

        try {
            //simple user authentication
            //user will get a more explicit authentication in the service
            //TODO refactor this later, duplicate checks
            if (service.authenticated(user, token) && user.getId().equals(userId)) {
                //user is authenticated
                System.out.println("Auth");
                User currentUser = service.updateProfile(user, token);
                //get currentuser
                if (currentUser != null) {
                    //if currentuser is not null, updating succeeded
                    return new ResponseEntity<User>(currentUser, HttpStatus.NO_CONTENT);
                } else {
                    //null means authenticating error failed in the service.updateProfile
                    throw new UserAuthenticationException();
                }
            } else {
                throw new UserAuthenticationException();
            }
        } catch (NullPointerException e) {
            System.out.println("auth went wrong in controller!");
            throw new UserAuthenticationException();
        }

    }

    @PutMapping("/logout")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> logoutUser(@RequestBody String token) {
        System.out.print("\n PUT /logout");
        System.out.print("got token: " + token);

        try {
            service.logout(service.getUserbyToken(token));
            return new ResponseEntity<String>("successful logout", HttpStatus.NO_CONTENT);
            // return 204
        } catch (Exception e) {
            //return 404
            return new ResponseEntity<>("An error occurred whilst logging out.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/token")

    public ResponseEntity<Void> validToken(@RequestHeader(value = "token") String token) {
        //to avoid refresh errors when server has been restarted and there still is a token in the localstorage
        //client sends GET and test if the token is still valid
        System.out.println("\ntoken//");
        System.out.println("token: " + token);
        if (service.getUserbyToken(token) != null) {
            System.out.println("token ok");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            System.out.println("token failed");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


    //LOGIN
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User curUser) {

        try {
            System.out.println("\nPOST /login");
            System.out.println("id: " + curUser.getId());
            System.out.println("uname: " + curUser.getUsername());
            System.out.println("pw: " + curUser.getPassword());
            log.info("\nPOST login");
//            System.err("\nPOST login");

            User regUser = service.login(curUser);
            if (regUser != null) {
                //Login succeeded
                return new ResponseEntity<User>(regUser, HttpStatus.OK);
            } else {
                //LOGIN failed
                System.out.println("login failed");
                throw new UserAuthenticationException();
            }

        } catch (Exception f) {
            System.out.println("throwing user does not exist exception");
            throw new UserDoesNotExistException();
        }
    }



}


