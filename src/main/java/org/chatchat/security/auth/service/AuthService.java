package org.chatchat.security.auth.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.security.auth.dto.request.RegisterDto;
import org.chatchat.user.domain.User;
import org.chatchat.user.service.UserQueryService;
import org.chatchat.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterDto registerDto) {

        String email = registerDto.email();
        userQueryService.validateIsDuplicatedEmail(email);

        String name = registerDto.name();
        userQueryService.validateIsDuplicatedName(name);

        String plainPassword = registerDto.password();
        String encodedPassword = passwordEncoder.encode(plainPassword);

        User newUser = new User(email, name, encodedPassword);
        userService.saveUser(newUser);
    }
}
