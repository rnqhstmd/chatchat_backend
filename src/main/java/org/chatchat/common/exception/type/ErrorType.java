package org.chatchat.common.exception.type;

import lombok.AllArgsConstructor;

import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // Token
    TOKEN_EXPIRED_ERROR("TOKEN_40100", "토큰의 유효 기간이 만료되었습니다."),
    TOKEN_NOT_INCLUDED_ERROR("TOKEN_40101", "요청에 토큰이 포함되지 않았습니다."),
    TOKEN_MALFORMED_ERROR("TOKEN_40102", "토큰 형식이 올바르지 않습니다."),
    TOKEN_TYPE_ERROR("TOKEN_40103", "토큰 타입이 일치하지 않거나 비어있습니다."),
    TOKEN_UNSUPPORTED_ERROR("TOKEN_40104", "지원하지 않는 토큰입니다."),
    TOKEN_UNKNOWN_ERROR("TOKEN_40105", "알 수 없는 토큰입니다."),

    // Cookie
    COOKIE_NOT_FOUND_ERROR("COOKIE_40100", "쿠키가 존재하지 않습니다."),

    // Auth
    NO_AUTHORIZATION_ERROR("AUTH_40100", "인증이 없는 사용자입니다."),
    ACCESS_DENIED_ERROR("AUTH_40300", "접근 권한이 없습니다."),

    // User
    DUPLICATED_EMAIL_ERROR( "USER_40900", "이미 사용 중인 이메일입니다."),
    DUPLICATED_NAME_ERROR( "USER_40901", "이미 사용 중인 이름입니다."),
    USER_NOT_FOUND_ERROR( "USER_40400", "해당 유저를 찾을 수 없습니다."),
    INVALID_CREDENTIAL_ERROR("USER_40100", "아이디나 비밀번호가 일치하지 않습니다."),

    // Room
    ROOM_NOT_FOUND_ERROR("ROOM_40400", "채팅방을 찾을 수 없습니다."),
    DUPLICATED_ROOM_NAME_ERROR("ROOM_40900", "이미 사용 중인 채팅방 이름입니다."),

    // RoomUser
    NO_PARTICIPANT_ROOM_ERROR("ROOM_USER_40400", "해당 채팅방에 참여자가 없습니다."),
    ALREADY_JOINED_USER_ERROR("ROOM_USER_40900", "이미 참가한 채팅방입니다."),
    NOT_ROOM_MEMBER_ERROR("ROOM_USER_40300", "채팅방의 멤버가 아닙니다."),

    // Resource
    NO_RESOURCE_ERROR( "RESOURCE_40000", "해당 리소스를 찾을 수 없습니다."),

    // HTTP
    METHOD_NOT_ALLOWED_ERROR( "HTTP_40500", "잘못된 HTTP 메서드입니다."),
    INVALID_REQUEST_PARAMETER_ERROR("HTTP_40000", "요청 파라미터가 잘못되었습니다."),
    MISSING_REQUEST_PARAMETER_ERROR("HTTP_40001", "필수 요청 파라미터가 누락되었습니다."),
    INVALID_REQUEST_FORMAT_ERROR("HTTP_40002", "요청에 필요한 데이터가 잘못되었습니다."),

    // Internal Server
    INTERNAL_SERVER_ERROR("INTERNAL_50000", "서버 내부 에러입니다."),
    WEBSOCKET_DISCONNECTED_ERROR("INTERNAL_50001", "웹소켓 연결이 끊어졌습니다."),

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
