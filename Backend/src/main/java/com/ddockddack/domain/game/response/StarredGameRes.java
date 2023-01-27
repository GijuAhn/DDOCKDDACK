package com.ddockddack.domain.game.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class StarredGameRes {
    private Long gameId;
    private String gameCategory;
    private String gameTitle;
    private String creator;
    private String regDate;
    private int isStarred;
    private int playCnt;
    private String thumbnail;

    @QueryProjection
    public StarredGameRes(Long gameId, String gameCategory, String gameTitle, String creator, String regDate, int isStarred, int playCnt, String thumbnail) {
        this.gameId = gameId;
        this.gameCategory = gameCategory;
        this.gameTitle = gameTitle;
        this.creator = creator;
        this.regDate = regDate;
        this.isStarred = isStarred;
        this.playCnt = playCnt;
        this.thumbnail = thumbnail;
    }
}
