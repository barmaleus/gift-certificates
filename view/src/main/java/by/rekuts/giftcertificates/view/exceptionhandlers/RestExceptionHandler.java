package by.rekuts.giftcertificates.view.exceptionhandlers;

import by.rekuts.giftcertificates.service.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // works when user creates/updates some entities with missed parameters
    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<Map<String, Object>> handleServiceException(Exception e, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        ServletWebRequest webRequest = (ServletWebRequest)request;
        HttpServletRequest servletRequest = webRequest.getRequest();
        ExceptionResponseBody responseBody = new ExceptionResponseBody(servletRequest, status, e);
        Map<String, Object> errorInfo = responseBody.getResponseBodyMap();
        return new ResponseEntity<>(errorInfo, status);
    }
}