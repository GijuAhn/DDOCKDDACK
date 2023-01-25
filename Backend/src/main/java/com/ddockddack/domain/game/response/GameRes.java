package com.ddockddack.domain.game.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameRes {
    private Long gameId;
    private String gameCategory;
    private String gameTitle;
    private String gameDesc;
    private String creator;
    private int isStarred;
    private int starredCnt;
    private int playCnt;
    private String thumbnail;

    @QueryProjection
    public GameRes(Long gameId, String gameCategory, String gameTitle, String gameDesc, String creator, int isStarred, int starredCnt, int playCnt, String thumbnail) {
        this.gameId = gameId;
        this.gameCategory = gameCategory;
        this.gameTitle = gameTitle;
        this.gameDesc = gameDesc;
        this.creator = creator;
        this.isStarred = isStarred;
        this.starredCnt = starredCnt;
        this.playCnt = playCnt;
        this.thumbnail = thumbnail;
    }
}
