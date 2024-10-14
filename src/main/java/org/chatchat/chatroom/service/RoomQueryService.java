package org.chatchat.chatroom.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatroom.domain.Room;
import org.chatchat.chatroom.domain.repository.RoomRepository;
import org.chatchat.chatroom.dto.response.RoomInfoResponse;
import org.chatchat.common.exception.ConflictException;
import org.chatchat.common.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.chatchat.common.exception.type.ErrorType.DUPLICATED_ROOM_NAME_ERROR;
import static org.chatchat.common.exception.type.ErrorType.ROOM_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class RoomQueryService {

    private final RoomRepository roomRepository;

    public List<RoomInfoResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> new RoomInfoResponse(room.getId(), room.getName()))
                .toList();
    }

    public Room findExistingRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND_ERROR));
    }

    public void validateExistingRoomByName(String roomName) {
        if (roomRepository.existsByName(roomName)) {
            throw new ConflictException(DUPLICATED_ROOM_NAME_ERROR);
        }
    }
}
