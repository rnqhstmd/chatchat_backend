package org.chatchat.chatroom.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatpart.domain.ChatPart;
import org.chatchat.chatpart.service.ChatPartQueryService;
import org.chatchat.chatpart.service.ChatPartService;
import org.chatchat.chatroom.domain.Room;
import org.chatchat.chatroom.domain.repository.RoomRepository;
import org.chatchat.chatroom.dto.request.CreateRoomRequest;
import org.chatchat.common.exception.ConflictException;
import org.chatchat.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.chatchat.common.exception.type.ErrorType.ALREADY_JOINED_USER_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomQueryService roomQueryService;
    private final ChatPartService chatPartService;
    private final ChatPartQueryService chatPartQueryService;

    public void createChatRoom(CreateRoomRequest createRoomRequest) {
        String name = createRoomRequest.name();
        roomQueryService.validateExistingRoomByName(name);
        Room room = new Room(name);
        roomRepository.save(room);
    }

    public void joinRoom(Long roomId, User user) {
        Room room = roomQueryService.findExistingRoomById(roomId);

        // 이미 채팅방에 참가했는지 확인
        if (chatPartQueryService.validateUserRoomMember(roomId, user.getId())) {
            throw new ConflictException(ALREADY_JOINED_USER_ERROR);
        }

        // 유저가 채팅방에 참여하도록 ChatPart 생성
        ChatPart chatPart = new ChatPart(room, user);
        chatPartService.saveChatPart(chatPart);
    }
}
