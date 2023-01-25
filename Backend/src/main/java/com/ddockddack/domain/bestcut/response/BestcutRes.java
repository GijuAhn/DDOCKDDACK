package com.ddockddack.domain.bestcut.response;


import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.criteria.CriteriaBuilder.In;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
public class BestcutRes {
    private Long bestcutId;
    private String bestcutImgTitle;
    private String bestcutImgUrl;
    private String gameTitle;
    private String gameImgUrl;
    private String gameImgDesc;
    private LocalDateTime createdDate;
    private Integer likeCnt;
    private Integer isLiked;
    private String profileImgUrl;
    private String nickname;

    @QueryProjection
    public BestcutRes(Long bestcutId, String bestcutImgTitle, String bestcutImgUrl,
        String gameTitle, String gameImgUrl, String gameImgDesc, LocalDateTime createdDate,
        Integer likeCnt, Integer isLiked, String profileImgUrl, String nickname) {
        this.bestcutId = bestcutId;
        this.bestcutImgTitle = bestcutImgTitle;
        this.bestcutImgUrl = bestcutImgUrl;
        this.gameTitle = gameTitle;
        this.gameImgUrl = gameImgUrl;
        this.gameImgDesc = gameImgDesc;
        this.createdDate = createdDate;
        this.likeCnt = likeCnt;
        this.isLiked = isLiked;
        this.profileImgUrl = profileImgUrl;
        this.nickname = nickname;
    }
}
