package com.ddockddack.domain.member.request;

import com.ddockddack.domain.game.request.GameImageModifyReq;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Schema(description = "MemberModifyImgRequest")
@NoArgsConstructor
public class MemberModifyImgReq {

//    @Valid
//    private List<MemberImageParam> images;

    private MultipartFile profileImg;

//    @Override
//    public String toString() {
//        return "MemberModifyImgReq{" +
//            ", images='" + Arrays.toString(images.toArray()) + '\'' +
//            '}';
//    }
}
