package org.chatchat.chatroom.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatpart.domain.ChatPart;
import org.chatchat.chatpart.service.ChatPartQueryService;
import org.chatchat.chatpart.service.ChatPartService;
import org.chatchat.chatroom.domain.Room;
import org.chatchat.chatroom.domain.repository.RoomRepository;
import org.chatchat.chatroom.dto.request.CreateRoomRequest;
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
    public void createChatRoom(CreateRoomRequest createRoomRequest, User user) {
        String name = createRoomRequest.name();
        // 채팅방 이름 중복 검사
        roomQueryService.validateExistingRoomByName(name);

        Room room = new Room(name);
        Room savedRoom = roomRepository.save(room);

        ChatPart chatPart = new ChatPart(savedRoom, user, user.getId());
        chatPartService.saveChatPart(chatPart);
    }

    /**
     * 채팅방 초대
     */
    public void inviteUserToRoom(Long roomId, Long userId, String username) {
        Room room = roomQueryService.findExistingRoomById(roomId);
        // 이미 채팅방에 참여 중인 유저 검증
        chatPartQueryService.isUserMemberOfRoom(roomId, userId);

        // 초대할 유저
        User inviteUser = userQueryService.findExistingUserByName(username);

        ChatPart chatPart = new ChatPart(room, inviteUser, userId);
        chatPartService.saveChatPart(chatPart);
    }

    /**
     * 채팅방 나가기
     */
    public void leaveRoom(Long roomId, Long userId) {
        ChatPart chatPart = chatPartQueryService.findExistingChatPart(roomId, userId);
        chatPartService.removeChatPart(chatPart);
    }
}
