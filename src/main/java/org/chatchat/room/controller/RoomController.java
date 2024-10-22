package org.chatchat.room.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chatchat.room.dto.request.CreateRoomRequest;
import org.chatchat.room.dto.request.InviteUserToRoomRequest;
import org.chatchat.room.dto.response.RoomInfoResponse;
import org.chatchat.room.service.RoomQueryService;
import org.chatchat.room.service.RoomService;
import org.chatchat.common.page.dto.response.PageResponseDto;
import org.chatchat.security.auth.annotation.AuthUser;
import org.chatchat.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomQueryService roomQueryService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<Void> createRoom(@RequestBody @Valid CreateRoomRequest createRoomRequest,
                                           @AuthUser User user) {
        roomService.createChatRoom(createRoomRequest, user);
        return ResponseEntity.ok().build();
    }

    // 참여 중인 채팅방 전체 조회
    @GetMapping
    public ResponseEntity<PageResponseDto<RoomInfoResponse>> getAllRooms(@RequestParam(defaultValue = "0") int page,
                                                                         @AuthUser User user) {
        PageResponseDto<RoomInfoResponse> rooms = roomQueryService.getAllRooms(page, user.getId());
        return ResponseEntity.ok(rooms);
    }

    // 채팅방 초대
    @PostMapping("/{roomId}/invite")
    public ResponseEntity<Void> inviteUserToRoom(@PathVariable Long roomId,
                                                 @AuthUser User user,
                                                 @RequestBody @Valid InviteUserToRoomRequest inviteRequest) {
        roomService.inviteUserToRoom(roomId, user.getId(), inviteRequest.username());
        return ResponseEntity.ok().build();
    }

    // 채팅방 나가기
    @DeleteMapping("/{roomId}/quit")
    public ResponseEntity<Void> quitRoom(@PathVariable Long roomId,
                                          @AuthUser User user) {
        roomService.quitRoom(roomId, user.getId());
        return ResponseEntity.ok().build();
    }
}
