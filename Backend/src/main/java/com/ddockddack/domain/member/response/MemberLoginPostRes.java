package com.ddockddack.domain.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "memberLoginPostResponse")
public class MemberLoginPostRes {
    @Schema(name = "JWT 인증 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN...")
    private String accessToken;

    private Long id;

    private String email;

    private String profile;


    public MemberLoginPostRes(String accessToken, Long id, String email, String profile) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.profile = profile;
    }
}
