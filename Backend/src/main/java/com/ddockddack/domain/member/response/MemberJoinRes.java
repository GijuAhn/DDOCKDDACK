package com.ddockddack.domain.member.response;

import com.ddockddack.domain.member.entity.Role;
import com.ddockddack.global.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "memberJoinResponse")
public class MemberJoinRes extends BaseResponseBody {

    Long memberId;

    String email;

    String nickname;

    String profile;

    Role role;

    public static MemberJoinRes of(Integer statusCode, String message, MemberJoinRes memberInfo) {
        MemberJoinRes res = new MemberJoinRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setEmail(memberInfo.getEmail());
        res.setNickname(memberInfo.getNickname());
//        res.setProfile(memberInfo.getProfile());
//        res.set(registerInfo.getGender());
        return res;
    }
}
