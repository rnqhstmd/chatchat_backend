package org.chatchat.security.auth.service;

import lombok.RequiredArgsConstructor;
import org.chatchat.common.exception.UnauthorizedException;
import org.chatchat.security.auth.dto.request.LoginRequest;
import org.chatchat.security.auth.dto.request.SignupRequest;
import org.chatchat.security.auth.dto.response.LoginResponse;
import org.chatchat.security.jwt.provider.JwtProvider;
import org.chatchat.user.domain.User;
import org.chatchat.user.service.UserQueryService;
import org.chatchat.user.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.chatchat.common.exception.type.ErrorType.INVALID_CREDENTIAL_ERROR;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void register(SignupRequest signUpRequest) {

        String email = signUpRequest.email();
        userQueryService.validateIsDuplicatedEmail(email);

        String name = signUpRequest.name();
        userQueryService.validateIsDuplicatedName(name);

        String rawPassword = signUpRequest.password();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User newUser = new User(email, name, encodedPassword);
        userService.saveUser(newUser);
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String rawPassword = (String) authentication.getCredentials();

        User user = userQueryService.findExistingUserByEmail(email);

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new UnauthorizedException(INVALID_CREDENTIAL_ERROR);
        }

        return new UsernamePasswordAuthenticationToken(user, rawPassword);
    }

    private LoginResponse getLoginResponse(final User user) {
        String jwtToken = jwtProvider.generateJwtToken(user.getEmail());
        return new LoginResponse(jwtToken);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        User user = (User) authenticate.getPrincipal();
        return getLoginResponse(user);
    }
}
