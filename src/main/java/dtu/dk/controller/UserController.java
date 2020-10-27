package dtu.dk.controller;

import dtu.dk.model.User;
import dtu.dk.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/password")
public class UserController {

    @Autowired
    private userRepository userrepository;

    @GetMapping("/AllUsers")
    public List<User> getAllUsers(){
        return userrepository.findAll();
    }

    @GetMapping("/UserById/{username}")
    public User getUserById(@PathVariable(value = "username") String userName) throws ResourceNotFoundException {
        return userrepository.findById(userName)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + userName));
    }

    @GetMapping("/AuthenticateUser/{username}/{password}")
    public Boolean authenticateUser(@PathVariable(value = "username") String username, @PathVariable(value = "password") String password){
        User user = new User(username, password);
        return user.equals(userrepository.findById(username));
    }
}
