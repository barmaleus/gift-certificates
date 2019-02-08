package by.rekuts.giftcertificates.view.exceptionhandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestOauth2EntryPoint extends OAuth2AuthenticationEntryPoint {

    @Override
    protected ResponseEntity enhanceResponse(ResponseEntity response, Exception exception) {
        ResponseEntity superEntity = super.enhanceResponse(response, exception);
        ExceptionResponseBody responseBody = new ExceptionResponseBody(response, exception);
        Map<String, Object> responseBodyMap = responseBody.getResponseBodyMap();

        return new ResponseEntity<Object>(responseBodyMap, superEntity.getHeaders(), response.getStatusCode());
    }
}
