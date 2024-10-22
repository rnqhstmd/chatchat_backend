package org.chatchat.roomuser.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.chatmessage.service.ChatMessageQueryService;
import org.chatchat.common.page.dto.response.PageResponseDto;
import org.chatchat.roomuser.service.RoomUserService;
import org.chatchat.security.auth.annotation.AuthUser;
import org.chatchat.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomUserController {

    private final RoomUserService roomUserService;
    private final ChatMessageQueryService chatMessageQueryService;

    // 채팅방 입장
    @GetMapping("/{roomId}/enter")
    public ResponseEntity<PageResponseDto<MessageResponse>> enterRoom(@PathVariable("roomId") Long roomId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @AuthUser User user,
                                                                      HttpServletRequest request) {
        // 세션 ID 추출
        String sessionId = request.getSession().getId();
        roomUserService.enterRoom(sessionId, roomId, user.getId());
        PageResponseDto<MessageResponse> messages = chatMessageQueryService.loadMessagesByRoomId(roomId, user.getId(), page);
        return ResponseEntity.ok(messages);
    }

    // 채팅방 나가기
    @GetMapping("/{roomId}/leave")
    public ResponseEntity<Void> leaveRoom(@PathVariable Long roomId,
                                          @AuthUser User user,
                                          HttpServletRequest request) {
        // 세션 ID 추출
        String sessionId = request.getSession().getId();
        roomUserService.leaveRoom(sessionId, roomId, user.getId());
        return ResponseEntity.ok().build();
    }
}
