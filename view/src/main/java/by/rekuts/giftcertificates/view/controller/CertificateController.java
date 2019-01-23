package by.rekuts.giftcertificates.view.controller;

import by.rekuts.giftcertificates.service.CertificateService;
import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.dto.CertificateDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CertificateController {
    private final CertificateService service;

    @Autowired
    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @GetMapping(value = "/certificates/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCertificateById(@PathVariable("certId") String certId) throws ServiceException {
        int id = Integer.parseInt(certId);
        CertificateDto certificateDto = service.getCertById(id);

        List<Link> links = getLinksForSingleCertificate(certificateDto);

        return new ResponseEntity<>(new Resource<>(certificateDto, links), HttpStatus.OK);
    }

    @GetMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<CertificateDto> getCertificates (
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "search", required = false) String search) throws ServiceException{

        Map<String, String> params = new HashMap<>();
        if(tag != null) { params.put("tag", tag); }
        if(search != null) { params.put("search", search); }

        List<Link> links = getLinksForCertificatesList();

        List<CertificateDto> certificates = params.size() == 0 ? service.getList() : service.getList(params);
        for(CertificateDto certificate : certificates) {
            certificate.add(linkToSingleCertificate(certificate));
        }
        return new Resources<>(certificates, links);
    }

    /**
     * Example of json object of certificate in request body
     *     {
     *         "name": "Certificate name",
     *             "description": null,
     *             "price": 500,
     *             "expirationDays": 5
     *     }
     */
    @PostMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createCertificate(
            @RequestBody CertificateDto newCertificate) throws ServiceException {
        String relativePathToCreatedElement = service.create(newCertificate);
        int certId = new ControllerHelper().getIdFromUrl(relativePathToCreatedElement);
        CertificateDto resultCertificate = service.getCertById(certId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", relativePathToCreatedElement);

        List<Link> links = getLinksForSingleCertificate(resultCertificate);

        return new ResponseEntity<>(new Resource<>(resultCertificate, links), headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/certificates/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateCertificate(
            @PathVariable("certId") String certId,
            @RequestBody CertificateDto upCertificate) throws ServiceException {
        int id = Integer.parseInt(certId);
        upCertificate.setCertificateId(id);
        service.update(upCertificate);

        List<Link> links = getLinksForSingleCertificate(upCertificate);

        return new ResponseEntity<>(new Resource<>(upCertificate, links), HttpStatus.OK);
    }

    @DeleteMapping(value = "/certificates/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDto> deleteCertificateById(@PathVariable("certId") String certId) {
        int id = Integer.parseInt(certId);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private List<Link> getLinksForCertificatesList() throws ServiceException {
        Link selfLink = linkTo(CertificateController.class).slash("certificates").withSelfRel();
        Link createLink = linkTo(methodOn(CertificateController.class)
                .createCertificate(new CertificateDto())).withRel("create-certificate");
        Link tagsLink = linkTo(TagController.class).slash("tags").withRel("all-tags");
        return Arrays.asList(selfLink, createLink, tagsLink);
    }

    private List<Link> getLinksForSingleCertificate(CertificateDto dto) throws ServiceException {
        Link selfLink = linkTo(CertificateController.class).slash("certificates/" + dto.getCertificateId()).withSelfRel();
        Link updateLink = linkTo(methodOn(CertificateController.class)
                .updateCertificate(String.valueOf(dto.getCertificateId()), new CertificateDto())).withRel("update-certificate");
        Link deleteLink = linkTo(methodOn(CertificateController.class)
                .deleteCertificateById(String.valueOf(dto.getCertificateId()))).withRel("delete-certificate");
        Link certsLink = linkTo(methodOn(CertificateController.class)
                .getCertificates(null, null)).withRel("all-certificates").expand();
        return Arrays.asList(selfLink, updateLink, deleteLink, certsLink);
    }

    private Link linkToSingleCertificate(CertificateDto certificate) throws ServiceException {
        return linkTo(methodOn(CertificateController.class)
                .getCertificateById(String.valueOf(certificate.getCertificateId())))
                .withRel("cert-" + certificate.getCertificateId());
    }
}