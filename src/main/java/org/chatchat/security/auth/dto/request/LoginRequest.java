package org.chatchat.security.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = "사용할 이메일을 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,30}$",
                message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{8,20}$",
                message = "비밀번호는 영문과 숫자,특수기호를 조합하여 8~20글자 미만으로 입력해주세요.")
        @NotBlank(message = "사용할 비밀번호를 입력해주세요.")
        String password
) {
}
