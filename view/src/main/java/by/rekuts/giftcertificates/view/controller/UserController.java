package by.rekuts.giftcertificates.view.controller;

import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.UserService;
import by.rekuts.giftcertificates.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable("id") String id) throws ServiceException {
        UserDto userDto = service.getUserById(Integer.parseInt(id));

        List<Link> links = getLinksForSingleUser(userDto);

        return new ResponseEntity<Resource>(new Resource<>(userDto, links), HttpStatus.OK);
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<UserDto> getUsers() throws ServiceException{
        List<UserDto> users = service.getList();

        List<Link> links = getLinksForUsersList();
        for(UserDto user : users) {
            user.add(linkToSingleUser(user));
        }
        return new Resources<>(users, links);
    }

    /**
     * Example of json object of user in request
     *     {
     *         "login": "user3",
     *         "password": "user3"
     *     }
     */
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createUser(@RequestBody UserDto newUser) throws ServiceException {
        String relativePathToCreatedElement = service.create(newUser);
        int userId = new ControllerHelper().getIdFromUrl(relativePathToCreatedElement);
        UserDto resultUser = service.getUserById(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", relativePathToCreatedElement);

        List<Link> links = getLinksForSingleUser(resultUser);

        return new ResponseEntity<>(new Resource<>(resultUser, links), headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateUser (
            @PathVariable("id") String userId,
            @RequestBody UserDto upUser) throws ServiceException {
        int id = Integer.parseInt(userId);
        upUser.setUserId(id);
        service.update(upUser);

        List<Link> links = getLinksForSingleUser(upUser);

        return new ResponseEntity<>(new Resource<>(upUser, links), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> deleteUserById(@PathVariable("id") String userId) {
        int id = Integer.parseInt(userId);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private List<Link> getLinksForUsersList() throws ServiceException {
        Link selfLink = linkTo(UserController.class).slash("users").withSelfRel();
        Link createLink = linkTo(methodOn(UserController.class).createUser(new UserDto())).withRel("create-user");
        Link tagsLink = linkTo(TagController.class).slash("tags").withRel("all-tags").expand();
        Link certsLink = linkTo(CertificateController.class).slash("certificates").withRel("all-certs").expand();
        return Arrays.asList(selfLink, createLink, tagsLink, certsLink);
    }

    private List<Link> getLinksForSingleUser(UserDto dto) throws ServiceException {
        Link selfLink = linkTo(UserController.class).slash("users/" + dto.getUserId()).withSelfRel();
        Link updateLink = linkTo(methodOn(UserController.class)
                .updateUser(String.valueOf(dto.getUserId()), new UserDto())).withRel("update-user");
        Link deleteLink = linkTo(methodOn(UserController.class).deleteUserById(String.valueOf(dto.getUserId())))
                .withRel("delete-user");
        Link usersLink = linkTo(methodOn(UserController.class).getUsers()).withRel("all-users");
        return Arrays.asList(selfLink, updateLink, deleteLink, usersLink);
    }

    private Link linkToSingleUser(UserDto user) throws ServiceException{
        return linkTo(methodOn(UserController.class)
                .getUser(String.valueOf(user.getUserId())))
                .withRel("tag-" + user.getUserId());
    }
}