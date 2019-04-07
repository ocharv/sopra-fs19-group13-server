//@Author Fabian Küffer 15-931-421
package ch.uzh.ifi.seal.soprafs19.service;



import ch.uzh.ifi.seal.soprafs19.controller.UserController;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private UserService userService;



    public static String asJsonString(final Object o){
        try{
            return new ObjectMapper().writeValueAsString(o);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }



    @Test
    public void loginTest() throws Exception{
        User user = new User();
        user.setUsername("user");
        user.setPassword("pw");
        user.setCreatedDate(null);
        user.setId(1L);

        when(userService.login(user)).thenReturn(user);

        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void logoutTest() throws Exception{
        User user = new User();
        user.setUsername("user");
        user.setPassword("pw");
        user.setCreatedDate(null);
        user.setId(1L);

        when(userService.logout(user)).thenReturn(user);

        this.mockMvc.perform(put("/logout")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(user)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }




    @Test
    public void registerTestWithDuplicateUserName() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pw");
        user.setCreatedDate(null);
        user.setId(1L);

        when(userService.userExists(user)).thenReturn(true);

        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(user))
        )
                .andDo(print())
                .andExpect(status().isConflict());

    }

    @Test
    public void registerTest() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pw");
        user.setCreatedDate(null);
        user.setId(1L);

        when(userService.createUser(user)).thenReturn(user);

        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("/users/1")));
    }


    @Test
    public void getUsersTest() throws Exception{

        User testUser1 = new User();
        testUser1.setUsername("user1");
        testUser1.setPassword("pw1");
        testUser1.setToken("asdf");
        User testUser2 = new User();
        testUser2.setUsername("user2");
        testUser2.setPassword("pw2");

        when(userService.getUsers(testUser1.getToken())).thenReturn(Arrays.asList(testUser1, testUser2));

        this.mockMvc.perform(get("/users")
                .header("token","asdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[1].username", is("user2")));
    }

}
