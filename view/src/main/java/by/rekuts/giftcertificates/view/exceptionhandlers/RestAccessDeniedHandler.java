package by.rekuts.giftcertificates.view.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class RestAccessDeniedHandler extends OAuth2AccessDeniedHandler {
    private OAuth2ExceptionRenderer exceptionRenderer = new DefaultOAuth2ExceptionRenderer();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        response.setStatus(status.value());
        ExceptionResponseBody responseBody = new ExceptionResponseBody(request, status, authException);
        Map<String, Object> errorInfo = responseBody.getResponseBodyMap();

        try {
            ResponseEntity<Map<String, Object>> result = new ResponseEntity<>(errorInfo, status);
            exceptionRenderer.handleHttpEntityResponse(result, new ServletWebRequest(request, response));
            response.flushBuffer();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
