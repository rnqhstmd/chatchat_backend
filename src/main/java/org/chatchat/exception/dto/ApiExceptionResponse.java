package org.chatchat.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.chatchat.exception.ApiException;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiExceptionResponse {

    private final int errorCode;
    private final String message;
    private final String detail;

    public static ApiExceptionResponse res(final ApiException apiException) {
        int errorCode = apiException.getErrorType().getErrorCode();
        String message = apiException.getErrorType().getMessage();
        String detail = apiException.getDetail();
        return new ApiExceptionResponse(errorCode, message, detail);
    }

    public static ApiExceptionResponse res(final Exception e) {
        int errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = HttpStatus.INTERNAL_SERVER_ERROR.name();
        String detail = e.getMessage();
        return new ApiExceptionResponse(errorCode, message, detail);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorCode=" + errorCode +
                ", message='" + message +
                ", detail='" + detail +
                '}';
    }
}
