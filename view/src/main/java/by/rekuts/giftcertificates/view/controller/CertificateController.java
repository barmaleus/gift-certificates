package by.rekuts.giftcertificates.view.controller;

import by.rekuts.giftcertificates.service.CertificateService;
import by.rekuts.giftcertificates.service.PurchaseService;
import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.UserService;
import by.rekuts.giftcertificates.service.dto.CertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.rekuts.giftcertificates.view.controller.HateoasLinksKeeper.*;

@RestController
public class CertificateController {
    private final CertificateService service;
    private final UserService userService;
    private final PurchaseService purchaseService;

    @Autowired
    public CertificateController(CertificateService service, UserService userService, PurchaseService purchaseService) {
        this.service = service;
        this.userService = userService;
        this.purchaseService = purchaseService;
    }

    @GetMapping(value = "/certificates/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCertificateById(@PathVariable("certId") int certId) throws ServiceException {
        CertificateDto certificateDto = service.getCertById(certId);

        List<Link> links = getLinksForSingleCertificate(certificateDto);

        return new ResponseEntity<>(new Resource<>(certificateDto, links), HttpStatus.OK);
    }

    @GetMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resources<CertificateDto> getCertificates (
            @RequestParam(value = "tag", required = false) String[] tag,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = ControllerHelper.PAGE_DEFAULT_VALUE) String page,
            @RequestParam(value = "item", defaultValue = ControllerHelper.ITEM_DEFAULT_VALUE) String item) throws ServiceException{

        Map<String, String[]> params = new HashMap<>();
        if(tag != null) { params.put("tag", tag); }
        if(search != null) { params.put("search", new String[]{search}); }
        int pageInt = new ControllerHelper().checkParameter(page);
        int itemInt = new ControllerHelper().checkParameter(item);

        List<Link> links = getLinksForCertificatesList();

        List<CertificateDto> certificates = params.size() == 0 ? service.getList(pageInt, itemInt) : service.getList(params, pageInt, itemInt);
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
            @RequestBody CertificateDto newCertificate, String csrfToken) throws ServiceException {
        String relativePathToCreatedElement = service.create(newCertificate);
        int certId = new ControllerHelper().getIdFromUrl(relativePathToCreatedElement);
        CertificateDto resultCertificate = service.getCertById(certId);

        HttpHeaders headers = new ControllerHelper()
                .addHeadersToResponseWhileCreateEntity(relativePathToCreatedElement, csrfToken);

        List<Link> links = getLinksForSingleCertificate(resultCertificate);

        return new ResponseEntity<>(new Resource<>(resultCertificate, links), headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/certificates/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateCertificate(
            @PathVariable("certId") String certId,
            @RequestBody CertificateDto upCertificate, String csrfToken) throws ServiceException {
        int id = Integer.parseInt(certId);
        upCertificate.setCertificateId(id);
        service.update(upCertificate);

        HttpHeaders headers = new ControllerHelper().addHeadersToSimpleResponse(csrfToken);

        List<Link> links = getLinksForSingleCertificate(upCertificate);

        return new ResponseEntity<>(new Resource<>(upCertificate, links), headers, HttpStatus.OK);
    }

    /**
     * Request body includes only double number without any braces, ex: 337.02
     */

    @PatchMapping(value = "/certificates/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> changePriceCertificate(
            @PathVariable("certId") String certId,
            @RequestBody BigDecimal price, String csrfToken) throws ServiceException {
        int id = Integer.parseInt(certId);
        service.changePrice(id, price);

        HttpHeaders headers = new ControllerHelper().addHeadersToSimpleResponse(csrfToken);

        CertificateDto patchedCertificate = service.getCertById(id);
        List<Link> links = getLinksForSingleCertificate(patchedCertificate);

        return new ResponseEntity<>(new Resource<>(patchedCertificate, links), headers, HttpStatus.OK);
    }

    @PostMapping(value = "/certificates/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> buyCertificate(
            @PathVariable("certId") String certId, String csrfToken) throws ServiceException {
        int id = Integer.parseInt(certId);
        String selfUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean certIsNotBoughtYet = purchaseService.buyCertificate(selfUsername, id);
        if (certIsNotBoughtYet) {
            HttpHeaders headers = new ControllerHelper().addHeadersToSimpleResponse(csrfToken);

            CertificateDto purchasedCertificate = service.getCertById(id);
            List<Link> links = getLinksForSingleCertificate(purchasedCertificate);

            return new ResponseEntity<>(new Resource<>(purchasedCertificate, links), headers, HttpStatus.OK);
        } else {
            HttpHeaders headers = new ControllerHelper().addHeadersToSimpleResponse(csrfToken);

            CertificateDto purchasedCertificate = service.getCertById(id);
            List<Link> links = getLinksForSingleCertificate(purchasedCertificate);

            return new ResponseEntity<>(new Resource<>(purchasedCertificate, links), headers, HttpStatus.FOUND);
        }

    }

    @DeleteMapping(value = "/certificates/{certId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCertificateById(@PathVariable("certId") String certId, String csrfToken) {
        return new ControllerHelper().deleteEntityById(service, certId, csrfToken);
    }


}