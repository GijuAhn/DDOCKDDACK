package com.ddockddack.domain.member.config;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {
    public UserDto toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return UserDto.builder()
                .email((String)attributes.get("email"))
                .nickname((String)attributes.get("nickname"))
                .profile((String)attributes.get("profile"))
                .build();
    }
}
