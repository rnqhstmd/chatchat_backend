package org.chatchat.chatroom.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatpart.domain.ChatPart;
import org.chatchat.chatpart.service.ChatPartService;
import org.chatchat.chatroom.domain.Room;
import org.chatchat.chatroom.domain.repository.RoomRepository;
import org.chatchat.chatroom.dto.request.CreateRoomRequest;
import org.chatchat.common.exception.NotFoundException;
import org.chatchat.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.chatchat.common.exception.type.ErrorType.ROOM_NOT_FOUND_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomQueryService roomQueryService;
    private final ChatPartService chatPartService;

    public void createChatRoom(CreateRoomRequest createRoomRequest) {
        String name = createRoomRequest.name();
        roomQueryService.validateExistingRoomByName(name);
        Room room = new Room(name);
        roomRepository.save(room);
    }

    public void joinRoom(Long roomId, User user) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND_ERROR));

        // 유저가 채팅방에 참여하도록 ChatPart 생성
        ChatPart chatPart = new ChatPart(room, user);
        chatPartService.saveChatPart(chatPart);
    }
}
