package org.chatchat.user.controller;

import lombok.RequiredArgsConstructor;
import org.chatchat.user.dto.response.SearchUserResponse;
import org.chatchat.user.service.UserQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService userQueryService;

    @GetMapping("/search")
    public ResponseEntity<List<SearchUserResponse>> searchUserByEmail(@RequestParam String email) {
        List<SearchUserResponse> searchUserResponses = userQueryService.searchUserByEmail(email);
        return ResponseEntity.ok(searchUserResponses);
    }
}
