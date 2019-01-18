package by.rekuts.giftcertificates.view.controller;

import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.UserService;
import by.rekuts.giftcertificates.service.dto.TagDto;
import by.rekuts.giftcertificates.service.dto.UserDto;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController.class.getName());
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable("id") String id) {
        UserDto userDto = service.getUserById(Integer.parseInt(id));
        Link selfLink = linkTo(UserController.class).slash("user/" + id).withSelfRel();
        Link usersLink = linkTo(methodOn(UserController.class).getUsers()).withRel("all-users");
        Link deleteLink = linkTo(methodOn(UserController.class).deleteUserById(String.valueOf(userDto.getUserId())))
                .withRel("delete-user");
        return userDto != null
                ? new ResponseEntity<Resource>(new Resource<>(userDto, selfLink, usersLink, deleteLink), HttpStatus.OK)
                : new ResponseEntity<TagDto>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<UserDto> getUsers() {
        List<UserDto> users = service.getList();
        Link selfLink = linkTo(UserController.class).slash("user").withSelfRel();
        Link tagsLink = linkTo(TagController.class).slash("tag").withRel("all-tags").expand();
        Link certsLink = linkTo(CertificateController.class).slash("certificate").withRel("all-certs").expand();
        Link createLink = linkTo(methodOn(UserController.class).createUser(new UserDto())).withRel("create-user");
        for(UserDto user : users) {
            user.add(linkToSingleUser(user));
        }
        return new Resources<>(users, selfLink, tagsLink, certsLink, createLink);
    }

    /**
     * Example of json object of user in request
     *     {
     *         "login": "user3",
     *             "password": "user3"
     *     }
     */

    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto newUser) {
        try {
            String relativePathToCreatedElement = service.create(newUser);
            int userId = new ControllerHelper().getIdFromUrl(relativePathToCreatedElement);
            UserDto resultUser = service.getUserById(userId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", relativePathToCreatedElement);
            return new ResponseEntity<>(resultUser, headers, HttpStatus.CREATED);
        } catch (ServiceException e) {
            LOGGER.log(Level.WARN, e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> deleteUserById(@PathVariable("id") String userId) {
        int id = Integer.parseInt(userId);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private Link linkToSingleUser(UserDto user) {
        return linkTo(methodOn(UserController.class)
                .getUser(String.valueOf(user.getUserId())))
                .withRel("tag-" + user.getUserId());
    }

    @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto upUser) {
        try {
            service.update(upUser);
            return new ResponseEntity<>(upUser, HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
