package by.rekuts.giftcertificates.view.controller;

import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.dto.CertificateDto;
import by.rekuts.giftcertificates.service.dto.TagDto;
import by.rekuts.giftcertificates.service.dto.UserDto;
import org.springframework.hateoas.Link;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class HateoasLinksKeeper {

    // links for certificates

    static List<Link> getLinksForCertificatesList() throws ServiceException {
        Link selfLink = linkTo(CertificateController.class).slash("certificates").withSelfRel();
        Link createLink = linkTo(methodOn(CertificateController.class)
                .createCertificate(new CertificateDto(), null)).withRel("create-certificate");
        Link tagsLink = linkTo(TagController.class).slash("tags").withRel("all-tags");
        return Arrays.asList(selfLink, createLink, tagsLink);
    }

    static List<Link> getLinksForSingleCertificate(CertificateDto dto) throws ServiceException {
        Link selfLink = linkTo(CertificateController.class).slash("certificates/" + dto.getCertificateId()).withSelfRel();
        Link updateLink = linkTo(methodOn(CertificateController.class)
                .updateCertificate(String.valueOf(dto.getCertificateId()), new CertificateDto(), null)).withRel("update-certificate");
        Link deleteLink = linkTo(methodOn(CertificateController.class)
                .deleteCertificateById(String.valueOf(dto.getCertificateId()), null)).withRel("delete-certificate");
        Link certsLink = linkTo(methodOn(CertificateController.class)
                .getCertificates(null, null, null, null)).withRel("all-certificates").expand();
        return Arrays.asList(selfLink, updateLink, deleteLink, certsLink);
    }

    static Link linkToSingleCertificate(CertificateDto certificate) throws ServiceException {
        return linkTo(methodOn(CertificateController.class)
                .getCertificateById(certificate.getCertificateId()))
                .withRel("cert-" + certificate.getCertificateId());
    }

    // links for users

    static List<Link> getLinksForUsersList() throws ServiceException {
        Link selfLink = linkTo(UserController.class).slash("users").withSelfRel();
        Link createLink = linkTo(methodOn(UserController.class).createUser(new UserDto(), null)).withRel("create-user");
        Link tagsLink = linkTo(TagController.class).slash("tags").withRel("all-tags").expand();
        Link certsLink = linkTo(CertificateController.class).slash("certificates").withRel("all-certs").expand();
        return Arrays.asList(selfLink, createLink, tagsLink, certsLink);
    }

    static List<Link> getLinksForSingleUser(UserDto dto) throws ServiceException {
        Link selfLink = linkTo(UserController.class).slash("users/" + dto.getUserId()).withSelfRel();
        Link updateLink = linkTo(methodOn(UserController.class)
                .updateUser(String.valueOf(dto.getUserId()), new UserDto(), null)).withRel("update-user");
        Link deleteLink = linkTo(methodOn(UserController.class).deleteUserById(String.valueOf(dto.getUserId()), null))
                .withRel("delete-user");
        Link usersLink = linkTo(methodOn(UserController.class).getUsers(null, null)).withRel("all-users");
        return Arrays.asList(selfLink, updateLink, deleteLink, usersLink);
    }

    static Link linkToSingleUser(UserDto user) throws ServiceException{
        return linkTo(methodOn(UserController.class)
                .getUser(user.getUserId()))
                .withRel("tag-" + user.getUserId());
    }

    //links for tags

    static List<Link> getLinksForTagsList() throws ServiceException {
        Link selfLink = linkTo(TagController.class).slash("tags").withSelfRel();
        Link createLink = linkTo(methodOn(TagController.class).createTag(new TagDto(), null)).withRel("create-tag");
        Link certsLink = linkTo(CertificateController.class).slash("certificates").withRel("all-certs").expand();
        return Arrays.asList(selfLink, createLink, certsLink);
    }

    static List<Link> getLinksForSingleTag(TagDto dto) throws ServiceException {
        Link selfLink = linkTo(TagController.class).slash("tags/" + dto.getName()).withSelfRel();
        Link deleteLink1 = linkTo(methodOn(TagController.class).deleteTagByName(dto.getName(), null)).withRel("delete-by-name");
        Link deleteLink2 = linkTo(methodOn(TagController.class).deleteTagById(String.valueOf(dto.getTagId()), null)).withRel("delete-by-id");
        Link tagsLink = linkTo(methodOn(TagController.class).getTags(null, null)).withRel("all-tags");
        return Arrays.asList(selfLink, deleteLink1, deleteLink2, tagsLink);
    }

    static Link linkToSingleTag(TagDto tag) throws ServiceException {
        return linkTo(methodOn(TagController.class)
                .getTag(tag.getName()))
                .withRel("tag-" + tag.getTagId());
    }
}
