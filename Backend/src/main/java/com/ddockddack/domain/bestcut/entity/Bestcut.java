package com.ddockddack.domain.bestcut.entity;

import com.ddockddack.domain.game.entity.GameImage;
import com.ddockddack.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Bestcut {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bestcut_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_bestcut_member_id_idx"))
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_image_id", foreignKey = @ForeignKey(name = "fk_bestcut_game_image_idx"))
    private GameImage gameImage;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(length = 300, nullable = false)
    private String imageUrl;

    @Column(columnDefinition = "DATETIME default now()")
    private LocalDateTime createdAt;

    @Builder
    public Bestcut(Member member, GameImage gameImage, String title, String imageUrl) {
        this.member = member;
        this.gameImage = gameImage;
        this.title = title;
        this.imageUrl = imageUrl;
    }
}
