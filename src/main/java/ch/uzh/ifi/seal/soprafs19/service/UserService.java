//@Author Fabian Küffer 15-931-421


package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.seal.soprafs19.Exception.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        //default spring template
        this.userRepository = userRepository;
    }


    public Iterable<User> getUsers(String token) {
        //returns all users in an Iterable
        //can easily get them into arraylist later

        if (authenticateWithTokenOnly(token)) {
            return this.userRepository.findAll();

        } else throw new UserAuthenticationException();
    }

    public boolean userExists(User user) {
        return getUserbyUserName(user.getUsername()) != null;
    }


    public User createUser(User newUser) {
        //creates a new user and saves it then
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        System.out.println("creating user");
        return newUser;
    }




    public User getUserById(long userId){
        //get an user by its id
        //throws exception otherwise
        try {
            return userRepository.findById(userId).get();
        } catch (NoSuchElementException e) {
            System.out.println("catched!");
            throw new UserNotFoundException(userId);
        }
    }


    public User getUserbyUserName(String uname){
        //returns a user by its userName
        return userRepository.findByUsername(uname);
    }

    public User getUserbyToken(String tok){
        //return a user by its token
        return userRepository.findByToken(tok);
    }


    public void updateUser(User user){
        //update a user in the database
        userRepository.save(user);
    }

    public void deleteUser(long userId){
        //deletes a specific user by userId
        System.out.println("deleting in service /deleteUser");

        userRepository.delete(getUserById(userId));
    }

    public void deleteUsers(){
        //deletes all users
        userRepository.deleteAll();
    }


    public User login(User user) {
        //login method
        //get registered user by the given username
        User regUser = getUserbyUserName(user.getUsername());
        //authenticate if username and password belong to same user
        if (regUser.getUsername().equals(user.getUsername()) && regUser.getPassword().equals(user.getPassword())) {
            //set the userstatus to ONLINE and update then return the user
            System.out.println("login succesfull");
            regUser.setStatus(UserStatus.ONLINE);
            updateUser(regUser);
            System.out.println("userid: " + regUser.getId());
            System.out.println("status " + regUser.getStatus());
            System.out.println("token: " + regUser.getToken());
            return regUser;
        } else {
            //auth failed
            //or throw exception TODO
            return null;
        }



    }
    public boolean authenticateWithTokenOnly(String token){
        //authenticated with Token only, used for general methods that only take the token and not an user as well
        if (getUserbyToken(token) != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean authenticated(User user, String token) {
        //checks if a token belongs to an user and if that user is currently ONLINE
        //if this is not the case most server methods wont be allowed to be called
        //in use in GET @/users/ and anything /users/{userId}
        System.out.println("user: " + user);
        System.out.println("user: " + getUserbyToken(token));
        System.out.println("stat: " + user.getStatus());

        return getUserbyToken(token).equals(user) && getUserbyToken(token).getStatus()== UserStatus.ONLINE;
    }

    public User logout(User user){
        //logout user
        System.out.print("\nservice logout");
        User regUser = getUserbyUserName(user.getUsername());
        System.out.print("Old status: " + user.getStatus());
        regUser.setStatus(UserStatus.OFFLINE);
        userRepository.save(regUser);
        return regUser;
    }


    public User updateProfile(User updatedUser, String token) {
        //updates an user, from the PUT REST mapping
        try {
            System.out.println("UserService: updateProfile");
            System.out.println("Got Token: " + token);
            System.out.println("Got id: " + updatedUser.getId());
            if (getUserById(updatedUser.getId()).equals(getUserbyToken(token))) {
                //authenticates user
                System.out.println("Got Token: " + token);
                System.out.println("Got id: " + updatedUser.getId());
                User oldUser = getUserbyToken(token);

                if (updatedUser.getUsername() != null) {
                    //if new username is not null there was a change
                    //update only then
                    System.out.println("new username is not null");
                    System.out.println("new uName: " + updatedUser.getUsername());
                    System.out.println("old uName: " + oldUser.getUsername());

                    if (getUserbyUserName(updatedUser.getUsername()) == null) {
                        //new username not taken
                        oldUser.setUsername(updatedUser.getUsername());

                    } else {
                        //username conflict
                        throw new UserDuplicateException(updatedUser.getUsername());
                    }
                }
                if (updatedUser.getBirthday() != null) {
                    //if new birthday date is not null there was a change
                    //update then
                    System.out.println("new birthday is not null");
                    System.out.println("new birthday: " + updatedUser.getBirthday());
                    System.out.println("old birthday: " + oldUser.getBirthday());
                    oldUser.setBirthday(updatedUser.getBirthday());
                }
                //save changes
                updateUser(oldUser);
                System.out.println("updated");
                return oldUser;
            } else {
                //Auth failed
                System.out.println("update: authentification failed, token does not match user");
                throw new UserAuthenticationException();
            }
        } catch (NullPointerException e) {
            //User not found
            System.out.println("User not found!");
            throw new UserNotFoundException(updatedUser.getId());
        }
    }


}
