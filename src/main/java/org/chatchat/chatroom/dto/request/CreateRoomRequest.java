package org.chatchat.chatroom.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateRoomRequest(
        @NotBlank(message = "생성할 채팅방 이름을 입력해주세요.")
        String name
) {
}
