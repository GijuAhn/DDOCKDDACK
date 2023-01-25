package com.ddockddack.domain.game.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GameDetailRes {

    private Long gameId;
    private String gameTitle;
    private String gameDesc;
    private List<GameImageRes> images;

    @QueryProjection
    public GameDetailRes(Long gameId, String gameTitle, String gameDesc, List<GameImageRes> images) {
        this.gameId = gameId;
        this.gameTitle = gameTitle;
        this.gameDesc = gameDesc;
        this.images = images;
    }
}
