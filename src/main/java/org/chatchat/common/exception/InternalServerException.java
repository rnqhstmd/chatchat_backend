package org.chatchat.common.exception;

import org.chatchat.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class InternalServerException extends ApiException {
    public InternalServerException(final ErrorType errorType) {
        super(errorType, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public InternalServerException(final ErrorType errorType, final String detail) {
        super(errorType, detail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
