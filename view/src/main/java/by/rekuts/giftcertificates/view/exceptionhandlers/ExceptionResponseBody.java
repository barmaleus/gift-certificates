package by.rekuts.giftcertificates.view.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

class ExceptionResponseBody {
    private Map<String,Object> responseBodyMap;

    ExceptionResponseBody(HttpServletRequest request, HttpStatus status, Exception exception) {
        responseBodyMap = new LinkedHashMap<>();
        responseBodyMap.put("time", LocalDate.now() + " " + LocalTime.now().withNano(0));
        responseBodyMap.put("status_code", status.value());
        responseBodyMap.put("status", status);
        responseBodyMap.put("error", exception.getClass().getSimpleName());
        responseBodyMap.put("message", exception.getMessage());
        responseBodyMap.put("localized_message", exception.getLocalizedMessage());
        responseBodyMap.put("path", request.getMethod() + " " + request.getRequestURL().toString());
    }

    ExceptionResponseBody(ResponseEntity<?> response, Exception exception) {
        responseBodyMap = new LinkedHashMap<>();
        responseBodyMap.put("time", LocalDate.now() + " " + LocalTime.now().withNano(0));
        responseBodyMap.put("status_code", response.getStatusCodeValue());
        responseBodyMap.put("status", response.getStatusCode());
        responseBodyMap.put("error", exception.getClass().getSimpleName());
        responseBodyMap.put("message", exception.getMessage());
        responseBodyMap.put("localized_message", exception.getLocalizedMessage());
    }

    Map<String, Object> getResponseBodyMap() {
        return responseBodyMap;
    }
}
