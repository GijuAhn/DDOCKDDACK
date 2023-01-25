package com.ddockddack.domain.game.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class GameImageModifyReq {

    private Long gameImageId;

    private MultipartFile gameImage;

    private String gameImageDesc;

}
