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
    private String profileImgUrl;
    private Role role;


    public MemberRes(Long memberId, String email, String nickname, String profileImgUrl, Role role) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.role = role;
    }
}
