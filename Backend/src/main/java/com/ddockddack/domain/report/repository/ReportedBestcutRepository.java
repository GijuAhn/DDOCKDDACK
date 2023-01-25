package com.ddockddack.domain.report.repository;

import com.ddockddack.domain.report.entity.ReportedBestcut;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportedBestcutRepository {

    private final EntityManager em;

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
}
