package com.ddockddack.domain.member.request;

import com.ddockddack.global.response.BaseResponseBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Schema(description = "MemberLoginRequest")
public class MemberModifyReq extends BaseResponseBody {
    @NotNull @Schema(name="유저 Name", example="ddockddack")
    String email;

    @NotBlank
    String nickname;

    @NotBlank
    String profile;

    @NotBlank
    String token;

}
