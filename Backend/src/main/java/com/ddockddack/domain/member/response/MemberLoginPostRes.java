package com.ddockddack.domain.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "memberLoginPostResponse")
public class MemberLoginPostRes {

    private String accessToken;
    private String refreshToken;
    private Long id;

    public MemberLoginPostRes(String accessToken, String refreshToken, Long id) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.id = id;
    }
}
