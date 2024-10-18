package org.chatchat.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteUserToRoomRequest(
        @NotNull(message = "초대할 채팅방의 id가 누락되었습니다.")
        Long roomId,
        @NotBlank(message = "초대할 사용자의 이름이 누락되었습니다.")
        String username
) {
}
