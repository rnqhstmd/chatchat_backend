package org.chatchat.security.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterDto(
        @NotBlank(message = "사용할 이메일을 입력해주세요.")
        String email,
        @NotBlank(message = "사용할 이름을 입력해주세요.")
        String name,
        @NotBlank(message = "사용할 비밀번호를 입력해주세요.")
        String password
) {
}
