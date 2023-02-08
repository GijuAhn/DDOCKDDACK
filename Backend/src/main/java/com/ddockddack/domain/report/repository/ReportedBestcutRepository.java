package com.ddockddack.domain.report.repository;

import com.ddockddack.domain.game.response.QReportedGameRes;
import com.ddockddack.domain.game.response.ReportedGameRes;
import com.ddockddack.domain.report.entity.ReportedBestcut;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.ddockddack.domain.member.entity.QMember.member;
import static com.ddockddack.domain.report.entity.QReportedGame.reportedGame;

@Repository
@RequiredArgsConstructor
public class ReportedBestcutRepository {

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public void save(ReportedBestcut reportedBestcut) {
        em.persist(reportedBestcut);
    }

    public Optional<ReportedBestcut> findOne(Long bestcutId, Long memberId) {
        List<ReportedBestcut> resultList = em.createQuery(
                "SELECT r " +
                    "FROM ReportedBestcut r " +
                    "WHERE r.bestcut.id = :bestcut AND r.reportMember.id = :member"
                , ReportedBestcut.class)
            .setParameter("bestcut", bestcutId)
            .setParameter("member", memberId)
            .getResultList();

        return resultList.stream().findAny();
    }

    // 신고된 게임 목록 조회
    public List<ReportedBestcut> findAllReportedBestCuts() {
        List<ReportedBestcut> resultList = em.createQuery(
                        "SELECT r " +
                                "FROM ReportedBestcut r "
                        , ReportedBestcut.class)
                .getResultList();

        return resultList;
    }
}
