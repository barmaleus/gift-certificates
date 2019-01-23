package by.rekuts.giftcertificates.view.controller;

import by.rekuts.giftcertificates.service.CertificateService;
import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.dto.CertificateDto;
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

    @GetMapping(value = "/certificate/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCertificateById(@PathVariable("certId") String certId) throws ServiceException {
        int id = Integer.parseInt(certId);
        CertificateDto certificateDto = service.getCertById(id);
        Link selfLink = linkTo(CertificateController.class).slash("certificate/" + id).withSelfRel();
        Link certsLink = linkTo(methodOn(CertificateController.class).getCertificates(null, null)).withRel("all-certificates").expand();
        Link updateLink = linkTo(methodOn(CertificateController.class).updateCertificate(String.valueOf(certificateDto.getCertificateId()), new CertificateDto())).withRel("update");
        Link deleteLink = linkTo(methodOn(CertificateController.class).deleteCertificateById(String.valueOf(certificateDto.getCertificateId()))).withRel("delete");
        return certificateDto != null
                ? new ResponseEntity<>(new Resource<>(certificateDto, selfLink, certsLink, updateLink, deleteLink), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<CertificateDto> getCertificates (
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "search", required = false) String search) throws ServiceException{

        Map<String, String> params = new HashMap<>();
        if(tag != null) { params.put("tag", tag); }
        if(search != null) { params.put("search", search); }
        Link selfLink = linkTo(CertificateController.class).slash("certificate").withSelfRel();
        Link tagsLink = linkTo(TagController.class).slash("tag").withRel("all-tags");
        Link createLink = linkTo(methodOn(CertificateController.class).createCertificate(new CertificateDto())).withRel("create-certificate");
        List<CertificateDto> certificates = params.size() == 0 ? service.getList() : service.getList(params);
        for(CertificateDto certificate : certificates) {
            certificate.add(linkToSingleCertificate(certificate));
        }
        return new Resources<>(certificates, selfLink, tagsLink, createLink);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////// example of request in the body //////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    /**
     * Example of json object of certificate in request
     *     {
     *         "name": "Certificate name",
     *             "description": null,
     *             "price": 500,
     *             "expirationDays": 5
     *     }
     */
    @PostMapping(value = "/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDto> createCertificate(
            @RequestBody CertificateDto newCertificate) throws ServiceException {
        String relativePathToCreatedElement = service.create(newCertificate);
        int certId = new ControllerHelper().getIdFromUrl(relativePathToCreatedElement);
        CertificateDto resultCertificate = service.getCertById(certId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", relativePathToCreatedElement);
        return new ResponseEntity<>(resultCertificate, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/certificate/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDto> updateCertificate(
            @PathVariable("certId") String certId,
            @RequestBody CertificateDto upCertificate) throws ServiceException {
        int id = Integer.parseInt(certId);
        upCertificate.setCertificateId(id);
        service.update(upCertificate);
        return new ResponseEntity<>(upCertificate, HttpStatus.OK);
    }

    @DeleteMapping(value = "/certificate/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDto> deleteCertificateById(@PathVariable("certId") String certId) {
        int id = Integer.parseInt(certId);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private Link linkToSingleCertificate(CertificateDto certificate) throws ServiceException {
        return linkTo(methodOn(CertificateController.class)
                .getCertificateById(String.valueOf(certificate.getCertificateId())))
                .withRel("cert-" + certificate.getCertificateId());
    }
}
