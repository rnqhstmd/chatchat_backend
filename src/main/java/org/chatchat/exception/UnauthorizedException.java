package org.chatchat.exception;

import org.chatchat.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {

    public UnauthorizedException(final ErrorType errorType) {
        super(errorType, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(final ErrorType errorType, final String detail) {
        super(errorType, detail, HttpStatus.UNAUTHORIZED);
    }
}