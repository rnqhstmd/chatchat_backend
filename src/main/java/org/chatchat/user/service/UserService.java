package org.chatchat.user.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.user.domain.User;
import org.chatchat.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void saveUser(final User user) {
        userRepository.save(user);
    }
}
