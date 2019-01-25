package by.rekuts.giftcertificates.view.controller;

import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.TagService;
import by.rekuts.giftcertificates.service.dto.TagDto;
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
public class TagController {
    private final TagService service;

    @Autowired
    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping(value = "/tags/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTag(@PathVariable("name") String name) throws ServiceException {
        TagDto tagDto = service.getTagByName(name);

        List<Link> links = getLinksForSingleTag(tagDto);

        return new ResponseEntity<Resource>(new Resource<>(tagDto, links), HttpStatus.OK);
    }

    @GetMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<TagDto> getTags() throws ServiceException {
        List<TagDto> tags = service.getList();

        List<Link> links = getLinksForTagsList();
        for(TagDto tag : tags) {
            tag.add(linkToSingleTag(tag));
        }

        return new Resources<>(tags, links);
    }

    /**
     * Example of json object of tag in request
     *     {
     *         "name": "Anytag"
     *     }
     */
    @PostMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createTag(@RequestBody TagDto newTag, String csrfToken) throws ServiceException {
        TagDto tagDto = service.getTagByName(newTag.getName());
        if (tagDto != null) {
            throw new ServiceException("Cannot crete new tag. Such tag exists.");
        } else {
            String relativePathToCreatedElement = service.create(newTag);
            TagDto resultTagDto = service.getTagByName(newTag.getName());

            HttpHeaders headers = new ControllerHelper()
                    .addHeadersToResponseWhileCreateEntity(relativePathToCreatedElement, csrfToken);

            List<Link> links = getLinksForSingleTag(resultTagDto);

            return new ResponseEntity<>(new Resource<>(resultTagDto, links), headers, HttpStatus.CREATED);
        }
    }

    @DeleteMapping(value = "/tags/id/{tagId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTagById(@PathVariable("tagId") String tagId, String csrfToken) {
        return new ControllerHelper().deleteEntityById(service, tagId, csrfToken);
    }

    @DeleteMapping(value = "/tags/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagDto> deleteTagByName(@PathVariable("name") String name, String csrfToken) throws ServiceException{
        TagDto tagDto = service.getTagByName(name);
        if (tagDto != null) {
            service.delete(tagDto.getTagId());

            HttpHeaders headers = new ControllerHelper().addHeadersToSimpleResponse(csrfToken);

            return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);
        } else {
            throw new ServiceException("Cannot delete tag. Tag with such name does not exist.");
        }
    }

    private List<Link> getLinksForTagsList() throws ServiceException {
        Link selfLink = linkTo(TagController.class).slash("tags").withSelfRel();
        Link createLink = linkTo(methodOn(TagController.class).createTag(new TagDto(), null)).withRel("create-tag");
        Link certsLink = linkTo(CertificateController.class).slash("certificates").withRel("all-certs").expand();
        return Arrays.asList(selfLink, createLink, certsLink);
    }

    private List<Link> getLinksForSingleTag(TagDto dto) throws ServiceException {
        Link selfLink = linkTo(TagController.class).slash("tags/" + dto.getName()).withSelfRel();
        Link deleteLink1 = linkTo(methodOn(TagController.class).deleteTagByName(dto.getName(), null)).withRel("delete-by-name");
        Link deleteLink2 = linkTo(methodOn(TagController.class).deleteTagById(String.valueOf(dto.getTagId()), null)).withRel("delete-by-id");
        Link tagsLink = linkTo(methodOn(TagController.class).getTags()).withRel("all-tags");
        return Arrays.asList(selfLink, deleteLink1, deleteLink2, tagsLink);
    }

    private Link linkToSingleTag(TagDto tag) throws ServiceException {
        return linkTo(methodOn(TagController.class)
                .getTag(tag.getName()))
                .withRel("tag-" + tag.getTagId());
    }
}
