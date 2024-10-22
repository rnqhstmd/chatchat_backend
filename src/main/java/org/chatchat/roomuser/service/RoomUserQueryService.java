package org.chatchat.roomuser.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.roomuser.domain.RoomUser;
import org.chatchat.roomuser.domain.repository.RoomUserRepository;
import org.chatchat.common.exception.ForbiddenException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.chatchat.common.exception.type.ErrorType.NOT_ROOM_MEMBER_ERROR;

@Service
@RequiredArgsConstructor
public class RoomUserQueryService {

    private final RoomUserRepository roomUserRepository;

    public void isUserMemberOfRoom(Long roomId, Long userId) {
        if (!roomUserRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new ForbiddenException(NOT_ROOM_MEMBER_ERROR);
        }
    }

    public RoomUser findExistingRoomUser(Long roomId, Long userId) {
        return roomUserRepository.findByRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new ForbiddenException(NOT_ROOM_MEMBER_ERROR));
    }

    public List<RoomUser> findExistingRoomUserListByRoomId(Long roomId) {
        return roomUserRepository.findByRoomId(roomId);
    }
}
