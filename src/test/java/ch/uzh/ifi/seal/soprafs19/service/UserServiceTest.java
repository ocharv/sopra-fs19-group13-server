//@Author Fabian Küffer 15-931-421


package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @After
    public void tearDown() throws Exception {
        //always delete all users after a test
        userService.deleteUsers();
    }


    @Test
    public void createUser() {
        //assert that this user does not exist yet
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");

        User createdUser = userService.createUser(testUser);
        //get token is not null
        Assert.assertNotNull(createdUser.getToken());
        //new user is not online yet
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE);
        //new user has a token
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
        //
        Assert.assertNotNull(userRepository.findByToken(createdUser.getToken()).getCreatedDate());

    }

    @Test
    public void getUsers(){

        //create user
        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        //create user2
        User testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername2");
        User createdUser2 = userService.createUser(testUser2);

        //assert count is 2
        Assert.assertEquals(2, userRepository.count());

        Iterable<User> users = userService.getUsers(createdUser.getToken());
        ArrayList<User> list = new ArrayList<>();
        //add content from users into an ArrayList
        for (User u : users) {
            list.add(u);
        }
        //assert that getUsers returned all users and their attributes
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("testUsername", list.get(0).getUsername());
        Assert.assertEquals("testUsername2", list.get(1).getUsername());
        Assert.assertEquals("testName", list.get(0).getPassword());
        Assert.assertEquals("testName2", list.get(1).getPassword());


    }


    @Test
    public void getUserById(){

        Assert.assertNull(userRepository.findByUsername("testUsername"));
        Assert.assertEquals(0, userRepository.count());

        //create user
        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        //create user2
        User testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername2");
        User createdUser2 = userService.createUser(testUser2);

//        assert users are equal
        Assert.assertEquals(createdUser, userService.getUserById(createdUser.getId()));
        Assert.assertEquals(createdUser2, userService.getUserById(createdUser2.getId()));


    }

    @Test
    public void getUserbyUserName(){
        Assert.assertNull(userRepository.findByUsername("testUsername"));
        Assert.assertEquals(0, userRepository.count());

        //create user
        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        //create user2
        User testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername2");
        User createdUser2 = userService.createUser(testUser2);
        Assert.assertEquals(createdUser, userService.getUserbyUserName(createdUser.getUsername()));
        Assert.assertEquals(createdUser2, userService.getUserbyUserName(createdUser2.getUsername()));
    }


    @Test
    public void getUserByToken(){
        Assert.assertNull(userRepository.findByUsername("testUsername"));
        Assert.assertEquals(0, userRepository.count());

        //create user
        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        //create user2
        User testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername2");
        User createdUser2 = userService.createUser(testUser2);
        Assert.assertEquals(createdUser, userService.getUserbyToken(createdUser.getToken()));
        Assert.assertEquals(createdUser2, userService.getUserbyToken(createdUser2.getToken()));
    }

    @Test
    public void updateUser(){
        Assert.assertNull(userRepository.findByUsername("testUsername"));
        Assert.assertEquals(0, userRepository.count());

        //create user
        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        //create user2
        User testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername2");
        User createdUser2 = userService.createUser(testUser2);

        Assert.assertEquals(createdUser.getUsername(), userService.getUserbyUserName("testUsername").getUsername());

        //without update it wont find by the new name, so it throws a nullpointerexception
        createdUser.setUsername("newName");
        try {
            Assert.assertNotEquals(createdUser.getUsername(), userService.getUserbyUserName("newName").getUsername());

        } catch (NullPointerException e) {
            //
            System.out.println("catched Nullpointer exception");
            userService.updateUser(createdUser);
            Assert.assertEquals(createdUser.getUsername(), userService.getUserbyUserName("newName").getUsername());
        }
    }


    @Test
    public void deleteUser(){
        //create user
        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        //create user2
        User testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername2");
        User createdUser2 = userService.createUser(testUser2);

        Assert.assertEquals(2, userRepository.count());
        userService.deleteUser(createdUser.getId());
        Assert.assertEquals(1, userRepository.count());
        userService.deleteUser(createdUser2.getId());
        Assert.assertEquals(0, userRepository.count());

    }


    @Test
    public void deleteUsers(){
        //create user
        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        //create user2
        User testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername2");
        User createdUser2 = userService.createUser(testUser2);

        Assert.assertEquals(2, userRepository.count());
        userService.deleteUsers();
        Assert.assertEquals(0, userRepository.count());
    }



    @Test
    public void login(){
        //create user
        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        //check offline
        Assert.assertEquals(UserStatus.OFFLINE, userRepository.findByToken(createdUser.getToken()).getStatus());
        userService.login(createdUser);
        //check that user is online
        Assert.assertEquals(UserStatus.ONLINE,userRepository.findByToken(createdUser.getToken()).getStatus());
    }

    @Test
    public void authenticated(){
        //create user
        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);
        //assert not authorized
        Assert.assertEquals(false, userService.authenticated(createdUser, createdUser.getToken()));
        //login user
        userService.login(createdUser);
        //assert true
        Assert.assertEquals(true, userService.authenticated(createdUser, createdUser.getToken()));
    }

    @Test
    public void updateProfile(){
        //create user
        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        //create user2
        User testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername2");
        testUser2.setId(createdUser.getId());



        User user3 = userService.updateProfile(testUser2, createdUser.getToken());
        Assert.assertEquals("testUsername2", user3.getUsername());

    }

}
