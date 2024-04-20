package com.nkd.eida_backend.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nkd.eida_backend.Domain.Response;
import com.nkd.eida_backend.Exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RequestUtils {

    private static final BiConsumer<HttpServletResponse, Response> writeResponse = (httpServletResponse, response) -> {
        try {
            var outputStream = httpServletResponse.getOutputStream();
            new ObjectMapper().writeValue(outputStream, response);
            outputStream.flush();
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    };

    public static Response createResponse(HttpServletRequest request, HttpStatus status, Map<?,?> data, String message) {
        return new Response(
                java.time.LocalDateTime.now().toString(),
                status.value(),
                request.getRequestURI(),
                status,
                message,
                data
        );
    }
    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception e) {
        if(e instanceof AccessDeniedException){
            var apiResponse = getErrorResponse(request, response, e, FORBIDDEN);
            writeResponse.accept(response, apiResponse);
        }
    }

    public static final BiFunction<Exception, HttpStatus, String> errorReason = ((exception, status) -> {
        if(status.isSameCodeAs(FORBIDDEN)) {return "You do not have permission";}
        if(status.isSameCodeAs(UNAUTHORIZED)) {return "You are not authorized";}
        if(exception instanceof DisabledException || exception instanceof LockedException || exception instanceof BadCredentialsException || exception instanceof ApiException){
            return exception.getMessage();
        }
        if(status.is5xxServerError()){
            return "An internal server error occurred";
        } else
            return "An error occurred";
    });

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception, HttpStatus status) {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        return new Response(
                java.time.LocalDateTime.now().toString(),
                status.value(),
                request.getRequestURI(),
                HttpStatus.valueOf(status.value()),
                errorReason.apply(exception, status),
                emptyMap());
    }
}
