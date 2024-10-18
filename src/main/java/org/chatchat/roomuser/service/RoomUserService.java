package org.chatchat.roomuser.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.roomuser.domain.RoomUser;
import org.chatchat.roomuser.domain.repository.RoomUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomUserService {

    private final RoomUserRepository roomUserRepository;

    public void saveChatPart(final RoomUser roomUser) {
        roomUserRepository.save(roomUser);
    }

    public void removeChatPart(final RoomUser roomUser) {
        roomUserRepository.delete(roomUser);
    }
}
