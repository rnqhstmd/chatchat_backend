package org.chatchat.common.exception.dto;

import org.chatchat.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public record ApiExceptionResponse(int errorCode, String message, String detail) {

    public static ApiExceptionResponse res(final ApiException apiException) {
        return new ApiExceptionResponse(
                apiException.getErrorType().getErrorCode(),
                apiException.getErrorType().getMessage(),
                apiException.getDetail()
        );
    }

    public static ApiExceptionResponse res(final Exception e) {
        return new ApiExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                e.getMessage()
        );
    }

    @Override
    public String toString() {
        return "ApiExceptionResponse {" +
                "errorCode=" + errorCode +
                ", message='" + message +
                ", detail='" + detail +
                '}';
    }
}
