package org.chatchat.exception;

import org.chatchat.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {

    public BadRequestException(final ErrorType errorType) {
        super(errorType, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(final ErrorType errorType, final String detail) {
        super(errorType, detail, HttpStatus.BAD_REQUEST);
    }
}
