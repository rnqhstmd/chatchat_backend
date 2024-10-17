package org.chatchat.chatroom.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.dto.response.MessageResponse;
import org.chatchat.chatmessage.service.ChatMessageQueryService;
import org.chatchat.chatroom.dto.request.CreateRoomRequest;
import org.chatchat.chatroom.dto.response.RoomInfoResponse;
import org.chatchat.chatroom.service.RoomQueryService;
import org.chatchat.chatroom.service.RoomService;
import org.chatchat.common.page.dto.response.PageResponseDto;
import org.chatchat.security.auth.annotation.AuthUser;
import org.chatchat.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomQueryService roomQueryService;
    private final ChatMessageQueryService chatMessageQueryService;

    @PostMapping
    public ResponseEntity<Void> createRoom(@RequestBody @Valid CreateRoomRequest createRoomRequest) {
        roomService.createChatRoom(createRoomRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RoomInfoResponse>> getAllRooms() {
        List<RoomInfoResponse> rooms = roomQueryService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    // 채팅방 참가
    @PostMapping("/{roomId}/join")
    public ResponseEntity<Void> joinRoom(@PathVariable("roomId") Long roomId,
                                         @AuthUser User user) {
        roomService.joinRoom(roomId, user);
        return ResponseEntity.ok().build();
    }

    // 채팅방 입장 및 이전 메시지 불러오기
    @GetMapping("/{roomId}/enter")
    public ResponseEntity<PageResponseDto<MessageResponse>> enterRoom(@PathVariable("roomId") Long roomId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @AuthUser User user) {
        PageResponseDto<MessageResponse> messages = chatMessageQueryService.loadMessagesByRoomId(roomId, user.getId(), page);
        return ResponseEntity.ok(messages);
    }
}
