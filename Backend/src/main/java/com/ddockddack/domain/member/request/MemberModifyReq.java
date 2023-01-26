package com.ddockddack.domain.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "MemberLoginRequest")
public class MemberModifyReq {
    @NotBlank
    String nickname;
    @NotBlank
    String profile;

}
