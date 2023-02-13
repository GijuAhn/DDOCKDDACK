package com.ddockddack.domain.gameRoom.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "game_room_history_member_id_idx"))
    private Member member;

    @Column(length = 30, nullable = false)
    private String GameTitle;

    @Column(nullable = false)
    private int ranking;

    @Column(columnDefinition = "DATE default (current_date)")
    private LocalDate createAt;

    @Builder
    public GameRoomHistory(Member member, String gameTitle, int ranking) {
        this.member = member;
        GameTitle = gameTitle;
        this.ranking = ranking;
    }
}
