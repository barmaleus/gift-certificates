package by.rekuts.giftcertificates.view.controller;

import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.TagService;
import by.rekuts.giftcertificates.service.dto.TagDto;
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
public class TagController {
    private static final Logger LOGGER = LogManager.getLogger(TagController.class.getName());
    private final TagService service;

    @Autowired
    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping(value = "/tag/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTag(@PathVariable("name") String name) {
        TagDto tagDto = service.getTagByName(name);
        Link selfLink = linkTo(TagController.class).slash("tag/" + name).withSelfRel();
        Link tagsLink = linkTo(methodOn(TagController.class).getTags()).withRel("all-tags");
        Link deleteLink1 = linkTo(methodOn(TagController.class).deleteTagByName(name)).withRel("delete-by-name");
        Link deleteLink2 = linkTo(methodOn(TagController.class).deleteTagById(String.valueOf(tagDto.getTagId()))).withRel("delete-by-id");
        return tagDto != null
                ? new ResponseEntity<Resource>(new Resource<>(tagDto, selfLink, tagsLink, deleteLink1, deleteLink2), HttpStatus.OK)
                : new ResponseEntity<TagDto>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/tag", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<TagDto> getTags() {
        List<TagDto> tags = service.getList();
        Link selfLink = linkTo(TagController.class).slash("tag").withSelfRel();
        Link certsLink = linkTo(CertificateController.class).slash("certificate").withRel("all-certs").expand();
        Link createLink = linkTo(methodOn(TagController.class).createTag("{tag-name}")).withRel("create-tag");
        for(TagDto tag : tags) {
            tag.add(linkToSingleTag(tag));
        }
        return new Resources<>(tags, selfLink, certsLink, createLink);
    }

    @PostMapping(value = "/tag/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> createTag(@PathVariable("name") String name) {
        TagDto tagDto = service.getTagByName(name);
        if (tagDto != null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            try {
                tagDto = new TagDto();
                tagDto.setName(name);
                String relativePathToCreatedElement = service.create(tagDto);
                TagDto resultTagDto = service.getTagByName(name);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", relativePathToCreatedElement);
                return new ResponseEntity<>(resultTagDto, headers, HttpStatus.CREATED);
            } catch (ServiceException e) {
                LOGGER.log(Level.WARN, e);
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @DeleteMapping(value = "/tag/id/{tagId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> deleteTagById(@PathVariable("tagId") String tagId) {
        int id = Integer.parseInt(tagId);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/tag/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> deleteTagByName(@PathVariable("name") String name) {
        TagDto tagDto = service.getTagByName(name);
        if (tagDto != null) {
            service.delete(tagDto.getTagId());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    private Link linkToSingleTag(TagDto tag) {
        return linkTo(methodOn(TagController.class)
                .getTag(tag.getName()))
                .withRel("tag-" + tag.getTagId());
    }
}
