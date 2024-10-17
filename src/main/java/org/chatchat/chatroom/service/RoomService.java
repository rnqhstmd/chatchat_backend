package org.chatchat.chatroom.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatpart.domain.ChatPart;
import org.chatchat.chatpart.service.ChatPartQueryService;
import org.chatchat.chatpart.service.ChatPartService;
import org.chatchat.chatroom.domain.Room;
import org.chatchat.chatroom.domain.repository.RoomRepository;
import org.chatchat.chatroom.dto.request.CreateRoomRequest;
import org.chatchat.chatroom.dto.request.InviteUserToRoomRequest;
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
    private final ChatPartService chatPartService;
    private final ChatPartQueryService chatPartQueryService;
    private final UserQueryService userQueryService;

    /**
     * 채팅방 생성
     */
    public void createChatRoom(CreateRoomRequest createRoomRequest) {
        String name = createRoomRequest.name();
        roomQueryService.validateExistingRoomByName(name);
        Room room = new Room(name);
        roomRepository.save(room);
    }

    /**
     * 채팅방 초대
     */
    public void inviteUserToRoom(Long roomId, InviteUserToRoomRequest inviteUserToRoomRequest) {
        Room room = roomQueryService.findExistingRoomById(roomId);
        User inviteUser = userQueryService.findExistingUserByName(inviteUserToRoomRequest.username());
        // 이미 채팅방에 참여 중인 유저 검증
        chatPartQueryService.isUserMemberOfRoom(roomId, inviteUser.getId());

        ChatPart chatPart = new ChatPart(room, inviteUser);
        chatPartService.saveChatPart(chatPart);
    }
}
