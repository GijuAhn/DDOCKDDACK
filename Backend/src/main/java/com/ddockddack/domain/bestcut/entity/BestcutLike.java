package com.ddockddack.domain.bestcut.entity;

import com.ddockddack.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BestcutLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bestcut_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bestcut_id", foreignKey = @ForeignKey(name = "fk_bestcut_like_bestcut_id_idx"))
    private Bestcut bestcut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_bestcut_like_member_id_idx"))
    private Member member;
}
