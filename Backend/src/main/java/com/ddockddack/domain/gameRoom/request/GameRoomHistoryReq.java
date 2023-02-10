package com.ddockddack.domain.gameRoom.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

@Getter @Setter
@ToString
@NoArgsConstructor
public class GameRoomHistoryReq {

    @Column(nullable = false)
    private Long memberId;

    @Column(length = 30, nullable = false)
    private String GameTitle;

    @Column(nullable = false)
    private int ranking;
}
