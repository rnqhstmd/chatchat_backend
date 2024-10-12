package org.chatchat.chatroom.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chatchat.chatroom.dto.request.CreateRoomRequest;
import org.chatchat.chatroom.service.RoomService;
import org.chatchat.security.auth.annotation.AuthUser;
import org.chatchat.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<Void> createRoom(@RequestBody @Valid CreateRoomRequest createRoomRequest) {
        roomService.createChatRoom(createRoomRequest);
        return ResponseEntity.ok().build();
    }

    // 채팅방 참가
    @PostMapping("/{roomId}/join")
    public ResponseEntity<Void> joinRoom(@PathVariable("roomId") Long roomId, @AuthUser User user) {
        roomService.joinRoom(roomId, user);
        return ResponseEntity.ok().build();
    }
}
