package com.ddockddack.domain.gameRoom.entity;

import com.ddockddack.domain.gameRoom.request.GameRoomHistoryReq;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.report.entity.ReportType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class GameRoomHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_room_history_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(length = 30, nullable = false)
    private String GameTitle;

    @Column(nullable = false)
    private int ranking;

    @Column(columnDefinition = "DATE default (current_date)")
    private LocalDate createAt;

    @Builder
    public GameRoomHistory(Long memberId, String gameTitle, int ranking) {
        this.memberId = memberId;
        GameTitle = gameTitle;
        this.ranking = ranking;
    }


    public static GameRoomHistory of(GameRoomHistoryReq gameRoomHistoryReq) {
        return GameRoomHistory.builder()
                .memberId(gameRoomHistoryReq.getMemberId())
                .gameTitle(gameRoomHistoryReq.getGameTitle())
                .ranking(gameRoomHistoryReq.getRanking())
                .build();
    }
}
