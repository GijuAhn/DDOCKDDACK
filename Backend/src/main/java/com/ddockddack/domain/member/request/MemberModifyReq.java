package com.ddockddack.domain.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Schema(description = "MemberModifyRequest")
public class MemberModifyReq {

    @NotBlank
    String nickname;

    @NotBlank
    String profile;
}
