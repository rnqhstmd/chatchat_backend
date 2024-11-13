package org.chatchat.room.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.roomuser.domain.RoomUser;
import org.chatchat.roomuser.service.RoomUserQueryService;
import org.chatchat.roomuser.service.RoomUserService;
import org.chatchat.room.domain.Room;
import org.chatchat.room.domain.repository.RoomRepository;
import org.chatchat.room.dto.request.CreateRoomRequest;
import org.chatchat.user.domain.User;
import org.chatchat.user.service.UserQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomQueryService roomQueryService;
    private final RoomUserService roomUserService;
    private final RoomUserQueryService roomUserQueryService;
    private final UserQueryService userQueryService;

    /**
     * 채팅방 생성
     */
    public void createChatRoom(CreateRoomRequest createRoomRequest, User user) {
        String name = createRoomRequest.name();
        // 채팅방 이름 중복 검사
        roomQueryService.validateExistingRoomByName(name);

        Room room = new Room(name);
        Room savedRoom = roomRepository.save(room);

        RoomUser roomUser = new RoomUser(savedRoom, user, user.getId(), 0);
        roomUserService.saveRoomUser(roomUser);
    }

    /**
     * 채팅방 초대
     */
    public void inviteUserToRoom(Long roomId, Long userId, String username) {
        Room room = roomQueryService.findExistingRoomByRoomId(roomId);
        // 이미 채팅방에 참여 중인 유저 검증
        roomUserQueryService.isUserMemberOfRoom(roomId, userId);

        // 초대할 유저
        User inviteUser = userQueryService.findExistingUserByName(username);

        RoomUser roomUser = new RoomUser(room, inviteUser, userId, 0);
        roomUserService.saveRoomUser(roomUser);
    }

    /**
     * 채팅방 탈퇴
     */
    public void quitRoom(Long roomId, Long userId) {
        RoomUser roomUser = roomUserQueryService.findExistingRoomUser(roomId, userId);
        roomUserService.removeRoomUser(roomUser);
    }
}
