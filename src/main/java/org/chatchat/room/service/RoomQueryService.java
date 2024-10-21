package org.chatchat.room.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.room.domain.Room;
import org.chatchat.room.domain.repository.RoomRepository;
import org.chatchat.room.dto.response.RoomInfoResponse;
import org.chatchat.common.exception.ConflictException;
import org.chatchat.common.exception.NotFoundException;
import org.chatchat.common.page.dto.request.PageRequestDto;
import org.chatchat.common.page.dto.response.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.chatchat.common.exception.type.ErrorType.*;

@Service
@RequiredArgsConstructor
public class RoomQueryService {

    private final RoomRepository roomRepository;

    /**
     * 참여 중인 채팅방 전체 조회
     */
    public PageResponseDto<RoomInfoResponse> getAllRooms(int page, Long userId) {
        PageRequestDto pageRequestDto = new PageRequestDto(page);
        Pageable pageable = pageRequestDto.toRoomPageable();

        Page<Room> roomPage = roomRepository.findByUserIdOrderedByLastChatDesc(userId, pageable);
        List<RoomInfoResponse> roomResponses = roomPage.getContent().stream()
                .map(room -> new RoomInfoResponse(room.getId(), room.getName()))
                .toList();

        return PageResponseDto.of(roomResponses, roomPage.getNumber(), roomPage.getTotalPages());
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
