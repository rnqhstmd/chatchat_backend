package org.chatchat.user.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.common.exception.ConflictException;
import org.chatchat.common.exception.NotFoundException;
import org.chatchat.user.domain.User;
import org.chatchat.user.domain.UserRepository;
import org.springframework.stereotype.Service;

import static org.chatchat.common.exception.type.ErrorType.*;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

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
