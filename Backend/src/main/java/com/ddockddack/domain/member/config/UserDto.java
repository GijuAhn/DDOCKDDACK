package com.ddockddack.domain.member.config;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private String email;
    private String nickname;
    private String profile;

    @Builder
    public UserDto(String email, String nickname, String profile) {
        this.email = email;
        this.nickname = nickname;
        this.profile = profile;
    }
}
