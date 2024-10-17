package org.chatchat.user.dto.response;

import org.chatchat.user.domain.User;

public record SearchUserResponse(
        Long id,
        String email,
        String username
) {
    public static SearchUserResponse from(User user) {
        return new SearchUserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername()
        );
    }
}
