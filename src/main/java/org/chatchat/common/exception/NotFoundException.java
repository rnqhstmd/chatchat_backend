package org.chatchat.common.exception;

import org.chatchat.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    public NotFoundException(final ErrorType errorType) {
        super(errorType, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(final ErrorType errorType, final String detail) {
        super(errorType, detail, HttpStatus.NOT_FOUND);
    }
}
