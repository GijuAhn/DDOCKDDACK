package com.ddockddack.domain.game.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportedGameRes {
    private Long reportId;
    private Long reportMemberId;
    private Long reportedMemberId;
    private Long gameId;
    private String reason;

    @QueryProjection
    public ReportedGameRes(Long reportId, Long reportMemberId, Long reportedMemberId, Long gameId, String reason) {
        this.reportId = reportId;
        this.reportMemberId = reportMemberId;
        this.reportedMemberId = reportedMemberId;
        this.gameId = gameId;
        this.reason = reason;
    }
}
