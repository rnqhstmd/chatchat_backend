package org.chatchat.security.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chatchat.security.auth.dto.request.LoginRequest;
import org.chatchat.security.auth.dto.request.SignupRequest;
import org.chatchat.security.auth.dto.response.LoginResponse;
import org.chatchat.security.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.chatchat.security.cookie.CookieUtil.addCookie;
import static org.chatchat.security.cookie.CookieUtil.clearCookies;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> register(@Valid @RequestBody SignupRequest signUpRequest) {
        authService.register(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest,
                                                   HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(loginRequest);
        addCookie(response, loginResponse.jwtToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getHeader("Set-Cookie"))
                .body(loginResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletResponse response) {
        clearCookies(response);
        return ResponseEntity.ok().build();
    }
}
