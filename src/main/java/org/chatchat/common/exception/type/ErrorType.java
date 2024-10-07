package org.chatchat.common.exception.type;

import lombok.AllArgsConstructor;

import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    //BAD_REQUEST
    JWT_PARSING_ERROR(40000,"JWT Token 형식이 올바르지 않습니다."),

    //UNAUTHORIZED
    JWT_EXPIRED_ERROR(40100, "Jwt Token의 유효 기간이 만료되었습니다."),
    JWT_NOT_INCLUDED_ERROR(40402, "요청에 Jwt Token이 포함되지 않았습니다."),

    //NOT_FOUND
    NO_RESOURCE_ERROR(40400, "해당 리소스를 찾을 수 없습니다."),
    USER_NOT_FOUND_ERROR(40401,"해당 유저를 찾을 수 없습니다."),

    //Http
    METHOD_NOT_ALLOWED_ERROR(40500, "잘못된 HTTP 메서드입니다."),

    //Validation
    NOT_NULL_VALID_ERROR(90100, "필수값이 누락되었습니다."),
    NOT_BLANK_VALID_ERROR(90101, "필수값이 빈 값이거나 공백으로 되어있습니다."),
    REGEX_VALID_ERROR(90102, "형식에 맞지 않습니다."),
    LENGTH_VALID_ERROR(90103, "길이가 유효하지 않습니다.");

    private final int errorCode;
    private final String message;

    public static ErrorType resolveValidationErrorCode(String code) {
        return switch (code) {
            case "NotNull" -> NOT_NULL_VALID_ERROR;
            case "NotBlank" -> NOT_BLANK_VALID_ERROR;
            case "Pattern" -> REGEX_VALID_ERROR;
            case "Length" -> LENGTH_VALID_ERROR;
            default -> throw new IllegalArgumentException("Unexpected value: " + code);
        };
    }
}
