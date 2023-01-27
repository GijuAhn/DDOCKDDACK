package com.ddockddack.domain.member.response;

import com.ddockddack.domain.member.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "memberInfoResponse")
public class MemberRes {
    private Long memberId;
    private String email;
    private String nickname;
    private String profile;
    private Role role;

    public MemberRes(String nickname, String profile) {
        this.nickname = nickname;
        this.profile = profile;
    }
}
