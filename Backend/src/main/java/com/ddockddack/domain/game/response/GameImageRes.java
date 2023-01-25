package com.ddockddack.domain.game.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameImageRes {

    private Long gameImageId;

    private String gameImage;

    private String gameImageDesc;

    @QueryProjection
    public GameImageRes(Long gameImageId, String gameImage, String gameImageDesc) {
        this.gameImageId = gameImageId;
        this.gameImage = gameImage;
        this.gameImageDesc = gameImageDesc;
    }


}
