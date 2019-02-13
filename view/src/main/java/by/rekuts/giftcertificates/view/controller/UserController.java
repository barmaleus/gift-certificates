package by.rekuts.giftcertificates.view.controller;

import by.rekuts.giftcertificates.service.CertificateService;
import by.rekuts.giftcertificates.service.PurchaseService;
import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.UserService;
import by.rekuts.giftcertificates.service.dto.CertificateDto;
import by.rekuts.giftcertificates.service.dto.PurchaseDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.rekuts.giftcertificates.view.controller.HateoasLinksKeeper.*;

@RestController
public class UserController {
    private final UserService service;
    private final CertificateService certificateService;
    private final PurchaseService purchaseService;

    @Autowired
    public UserController(UserService service, CertificateService certificateService, PurchaseService purchaseService) {
        this.service = service;
        this.certificateService = certificateService;
        this.purchaseService = purchaseService;
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable("id") int id) throws ServiceException {
        UserDto userDto = service.getUserById(id);

        List<Link> links = getLinksForSingleUser(userDto);

        return new ResponseEntity<Resource>(new Resource<>(userDto, links), HttpStatus.OK);
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<UserDto> getUsers(
            @RequestParam(value = "page", defaultValue = ControllerHelper.PAGE_DEFAULT_VALUE) String page,
            @RequestParam(value = "item", defaultValue = ControllerHelper.ITEM_DEFAULT_VALUE) String item) throws ServiceException {
        int pageInt = new ControllerHelper().checkParameter(page);
        int itemInt = new ControllerHelper().checkParameter(item);
        List<UserDto> users = service.getList(pageInt, itemInt);

        List<Link> links = getLinksForUsersList();
        for(UserDto user : users) {
            user.add(linkToSingleUser(user));
        }
        return new Resources<>(users, links);
    }

    @GetMapping(value = "/users/{userId}/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<CertificateDto> getUsersCertificates(
            @PathVariable("userId") int id,
            @RequestParam(value = "tag", required = false) String[] tag,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = ControllerHelper.PAGE_DEFAULT_VALUE) String page,
            @RequestParam(value = "item", defaultValue = ControllerHelper.ITEM_DEFAULT_VALUE) String item) throws ServiceException {
        UserDto userDto = service.getUserById(id);

        Map<String, String[]> params = new HashMap<>();
        if(tag != null) { params.put("tag", tag); }
        if(search != null) { params.put("search", new String[]{search}); }
        params.put("userId", new String[]{String.valueOf(id)});
        int pageInt = new ControllerHelper().checkParameter(page);
        int itemInt = new ControllerHelper().checkParameter(item);

        List<Link> links = getLinksForSingleUser(userDto);

        List<CertificateDto> certificates = certificateService.getList(params, pageInt, itemInt);
        for(CertificateDto certificate : certificates) {
            certificate.add(linkToSingleCertificate(certificate));
            certificate.add(purchaseLink(userDto, certificate));
        }
        return new Resources<>(certificates, links);
    }

    @GetMapping(value = "/users/{userId}/certificate/{certId}/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> getUsersCertificatePurcase(
            @PathVariable("userId") int userId,
            @PathVariable("certId") int certId) throws ServiceException {
        UserDto user = service.getUserById(userId);
        CertificateDto certificate = certificateService.getCertById(certId);

        Link userLink = linkToSingleUser(user);
        Link certLink = linkToSingleCertificate(certificate);

        PurchaseDto purchase = purchaseService.getList(userId, certId, null, null);
        return new ResponseEntity<>(new Resource<>(purchase, userLink, certLink), HttpStatus.OK);
    }

    /**
     * Example of json object of user in request
     *     {
     *         "login": "user3",
     *         "password": "user3"
     *     }
     */
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createUser(@RequestBody UserDto newUser, String csrfToken) throws ServiceException {
        String relativePathToCreatedElement = service.create(newUser);
        int userId = new ControllerHelper().getIdFromUrl(relativePathToCreatedElement);
        UserDto resultUser = service.getUserById(userId);

        HttpHeaders headers = new ControllerHelper()
                .addHeadersToResponseWhileCreateEntity(relativePathToCreatedElement, csrfToken);

        List<Link> links = getLinksForSingleUser(resultUser);

        return new ResponseEntity<>(new Resource<>(resultUser, links), headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateUser (
            @PathVariable("id") String userId,
            @RequestBody UserDto upUser, String csrfToken) throws ServiceException {
        int id = Integer.parseInt(userId);
        upUser.setUserId(id);
        service.update(upUser);

        HttpHeaders headers = new ControllerHelper().addHeadersToSimpleResponse(csrfToken);

        List<Link> links = getLinksForSingleUser(upUser);

        return new ResponseEntity<>(new Resource<>(upUser, links), headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteUserById(@PathVariable("id") String userId, String csrfToken) {
        return new ControllerHelper().deleteEntityById(service, userId, csrfToken);
    }
}