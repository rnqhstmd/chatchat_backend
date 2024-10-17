package org.chatchat.user.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.common.exception.ConflictException;
import org.chatchat.common.exception.NotFoundException;
import org.chatchat.user.domain.User;
import org.chatchat.user.domain.repository.UserRepository;
import org.chatchat.user.dto.response.SearchUserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.chatchat.common.exception.type.ErrorType.*;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    /**
     * 이메일로 유저 검색
     */
    public List<SearchUserResponse> searchUserByEmail(String email) {
        List<User> users = userRepository.findByEmailContaining(email);
        return users.stream()
                .map(SearchUserResponse::from)
                .toList();
    }

    public User findExistingUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_ERROR));
    }

    public void validateIsDuplicatedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw  new ConflictException(DUPLICATED_EMAIL_ERROR);
        }
    }

    public void validateIsDuplicatedName(String name) {
        if (userRepository.existsByUsername(name)) {
            throw  new ConflictException(DUPLICATED_NAME_ERROR);
        }
    }
}
