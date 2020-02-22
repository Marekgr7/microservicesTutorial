package gryszq.service.microservices.restController;

import gryszq.service.microservices.exception.UserNotFoundException;
import gryszq.service.microservices.model.User;
import gryszq.service.microservices.service.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserRestController {

    private UserDaoService userDaoService;

    @Autowired
    public UserRestController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return userDaoService.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id){
        User user = userDaoService.findOne(id);
        if(user == null){
            throw new UserNotFoundException("id-"+id);
        }
        //HATEOS
        //"all-users", SERVER_PATH + "/users"
        //retrieveAllUsers
        //FIRST STEP WE ARE CREATING MODEL - WILL BE ADDED TO OUR RESPONSE
        EntityModel<User> model = new EntityModel<>(user);
        //WE ARE CREATING LINK FOR THIS CLASS AND METHOD
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        //WE ARE RETURNING MODEL WITH LINK TO GET ALL USERS
        model.add(linkTo.withRel("all-users"));
        return model;
    }

    @PostMapping("/user")
    public ResponseEntity<Object> saveUser(@Valid @RequestBody User user){
        User savedUser = userDaoService.save(user);

        //We want to return as a response a path to get details of created user
        //First step we are using servletUriComponentsBuilder and we are adding to existing path user ID
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        //now we are returning response we status 201 - created and passing location to response header
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable int id){
        User user = userDaoService.deleteUser(id);
        if(user == null){
            throw new UserNotFoundException("id-"+id);
        }
        return user;
    }
}
