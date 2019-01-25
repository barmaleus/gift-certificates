package by.rekuts.giftcertificates.view.controller;

import by.rekuts.giftcertificates.service.CrudService;
import by.rekuts.giftcertificates.view.security.OAuth2ResourceServerConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ControllerHelper {

    ResponseEntity deleteEntityById(CrudService service, String entityId, String csrfToken) {
        int id = Integer.parseInt(entityId);
        service.delete(id);

        HttpHeaders headers = new ControllerHelper().addHeadersToSimpleResponse(csrfToken);

        return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);
    }

    int getIdFromUrl(String pathToCreatedElement) {
        int slashIndex = pathToCreatedElement.lastIndexOf("/");
        String substr = pathToCreatedElement.substring(slashIndex+1);
        return Integer.parseInt(substr);
    }

    HttpHeaders addHeadersToResponseWhileCreateEntity(String location, String csrfToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", location);
        return getHttpHeaders(csrfToken, headers);
    }

    HttpHeaders addHeadersToSimpleResponse(String csrfToken) {
        HttpHeaders headers = new HttpHeaders();
        return getHttpHeaders(csrfToken, headers);
    }

    private HttpHeaders getHttpHeaders(String csrfToken, HttpHeaders headers) {
        if (csrfToken != null) {
            headers.set(HttpHeaders.COOKIE, OAuth2ResourceServerConfig.CSRF_TOKEN + "=" + csrfToken);
            headers.set(OAuth2ResourceServerConfig.X_CSRF_TOKEN, csrfToken);
        }
        return headers;
    }
}
