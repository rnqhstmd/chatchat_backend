package org.chatchat.roomuser.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.chatmessage.service.ChatMessageQueryService;
import org.chatchat.room.service.RoomQueryService;
import org.chatchat.room.util.RoomSessionManager;
import org.chatchat.roomuser.domain.RoomUser;
import org.chatchat.roomuser.domain.repository.RoomUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomUserService {

    private final RoomUserRepository roomUserRepository;
    private final RoomUserQueryService roomUserQueryService;
    private final ChatMessageQueryService chatMessageQueryService;
    private final RoomQueryService roomQueryService;
    private final RoomSessionManager roomSessionManager;

    public void saveRoomUser(final RoomUser roomUser) {
        roomUserRepository.save(roomUser);
    }

    public void removeRoomUser(final RoomUser roomUser) {
        roomUserRepository.delete(roomUser);
    }

    public void increaseUnreadMessageCount(Long roomId, String senderName) {
        List<RoomUser> roomUsers = roomUserQueryService.findExistingRoomUserListByRoomId(roomId);

        // 읽지 않은 메세지 수를 증가시킬 사용자들
        List<RoomUser> usersToUpdate = roomUsers.stream()
                .filter(roomUser -> !roomUser.getUser().getUsername().equals(senderName))
                .filter(roomUser -> {
                    String sessionId = roomSessionManager.getSessionIdForUser(roomUser.getUser().getId());
                    return sessionId == null || !roomSessionManager.isSessionInRoom(sessionId, String.valueOf(roomId));
                })
                .toList();
        // 읽지 않은 메세지 수 증가 처리
        usersToUpdate.forEach(RoomUser::incrementCount);

        roomUserRepository.saveAll(usersToUpdate);
    }

    /**
     * 채팅방 입장
     */
    public void enterRoom(String sessionId, Long roomId, Long userId) {
        // 채팅방에 입장 처리
        roomSessionManager.enterRoom(sessionId, roomId.toString());
        roomSessionManager.mapUserSession(userId, sessionId);

        roomQueryService.validateExistingRoomByRoomId(roomId);
        RoomUser roomUser = roomUserQueryService.findExistingRoomUser(roomId, userId);
        // 안읽은 채팅 개수 0개로 초기화
        roomUser.resetCount();
        saveRoomUser(roomUser);
    }

    /**
     * 채팅방 나가기
     */
    public void leaveRoom(String sessionId, Long roomId, Long userId) {
        String sessionRoomId = roomSessionManager.getCurrentRoom(sessionId);
        if (sessionRoomId != null && sessionRoomId.equals(roomId.toString())) {
            roomSessionManager.leaveRoom(sessionId);
            RoomUser roomUser = roomUserQueryService.findExistingRoomUser(roomId, userId);
            roomUser.updateLastReadMessageId(chatMessageQueryService.getLatestMessageId(String.valueOf(roomId)));
            roomUserRepository.save(roomUser);
        }
    }
}
