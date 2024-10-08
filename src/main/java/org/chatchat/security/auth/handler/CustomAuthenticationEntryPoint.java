package org.chatchat.security.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.chatchat.common.exception.ApiException;
import org.chatchat.common.exception.UnauthorizedException;
import org.chatchat.common.exception.type.ErrorType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.chatchat.common.exception.type.ErrorType.JWT_NOT_INCLUDED_ERROR;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        if (authException.getCause() instanceof ApiException apiException) {
            setResponse(response, apiException);
            return;
        }

        ApiException apiException = new UnauthorizedException(JWT_NOT_INCLUDED_ERROR);
        setResponse(response, apiException);
    }

    private void setResponse(HttpServletResponse response, ApiException apiException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ErrorType errorType = apiException.getErrorType();
        int status = apiException.getHttpStatus().value();
        response.setStatus(status);

        response.getWriter().println(
                "{\"status\" : \"" + status + "\"," +
                        "\"errorCode\" : \"" + errorType.getErrorCode() + "\"," +
                        "\"message\" : \"" + errorType.getMessage() + "\"}");
    }
}
