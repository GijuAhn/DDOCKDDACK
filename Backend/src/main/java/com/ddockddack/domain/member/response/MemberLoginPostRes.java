package com.ddockddack.domain.member.response;

import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.global.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "memberLoginPostResponse")
public class MemberLoginPostRes extends BaseResponseBody {
    @Schema(name = "JWT 인증 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN...")
    private String accessToken;

    private Long id;

    private String email;

    private String profile;

    public static MemberLoginPostRes of(Integer statusCode, String message, Member member, String accessToken) {
        MemberLoginPostRes res = new MemberLoginPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setAccessToken(accessToken);
        res.setId(member.getId());
        res.setEmail(member.getEmail());
        res.setProfile(member.getProfile());
        return res;
    }
}
