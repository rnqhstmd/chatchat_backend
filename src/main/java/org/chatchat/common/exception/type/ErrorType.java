package org.chatchat.common.exception.type;

import lombok.AllArgsConstructor;

import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    //JWT
    JWT_PARSING_ERROR("JWT_40000", "JWT Token 형식이 올바르지 않습니다."),
    JWT_EXPIRED_ERROR("JWT_40100", "Jwt Token의 유효 기간이 만료되었습니다."),
    JWT_NOT_INCLUDED_ERROR("JWT_40101", "요청에 Jwt Token이 포함되지 않았습니다."),

    // Auth
    NO_AUTHORIZATION_ERROR("AUTH_40100", "인증이 없는 사용자입니다."),

    // User
    DUPLICATED_EMAIL_ERROR( "USER_40900", "이미 사용 중인 이메일입니다."),
    DUPLICATED_NAME_ERROR( "USER_40901", "이미 사용 중인 이름입니다."),
    USER_NOT_FOUND_ERROR( "USER_40400", "해당 유저를 찾을 수 없습니다."),
    INVALID_CREDENTIAL_ERROR("USER_40100", "아이디나 비밀번호가 일치하지 않습니다."),

    // Resource
    NO_RESOURCE_ERROR( "RESOURCE_40000", "해당 리소스를 찾을 수 없습니다."),

    // HTTP
    METHOD_NOT_ALLOWED_ERROR( "HTTP_40500", "잘못된 HTTP 메서드입니다."),

    // Validation
    NOT_NULL_VALID_ERROR( "VALID_90000", "필수값이 누락되었습니다."),
    NOT_BLANK_VALID_ERROR( "VALID_90001", "필수값이 빈 값이거나 공백으로 되어있습니다."),
    REGEX_VALID_ERROR( "VALID_90002", "형식에 맞지 않습니다."),
    LENGTH_VALID_ERROR("VALID_90003", "길이가 유효하지 않습니다.");

    private final String errorCode;
    private final String message;

    public static ErrorType resolveValidationErrorCode(String code) {
        return switch (code) {
            case "NotNull" -> NOT_NULL_VALID_ERROR;
            case "NotBlank" -> NOT_BLANK_VALID_ERROR;
            case "Pattern" -> REGEX_VALID_ERROR;
            case "Size" -> LENGTH_VALID_ERROR;
            default -> throw new IllegalArgumentException("Unexpected value: " + code);
        };
    }
}
