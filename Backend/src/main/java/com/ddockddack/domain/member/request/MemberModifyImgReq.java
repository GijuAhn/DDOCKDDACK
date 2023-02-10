package com.ddockddack.domain.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Schema(description = "MemberModifyImgRequest")
public class MemberModifyImgReq {
    @NotBlank(message = "프로필 사진을 설정해주세요.")
    private MultipartFile profile;

    @Override
    public String toString() {
        return "MemberModifyReq{" +
            ", profile='" + profile + '\'' +
            '}';
    }
}
