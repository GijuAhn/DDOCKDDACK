package com.ddockddack.domain.bestcut.repository;

import com.ddockddack.domain.bestcut.entity.BestcutLike;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BestcutLikeRepository {

    private final EntityManager em;


    public Long save(BestcutLike bestcutLike) {
        em.persist(bestcutLike);
        return bestcutLike.getId();
    }

    public Optional<BestcutLike> findOne(Long bestcutId, Long memberId) {
        List<BestcutLike> resultList = em.createQuery(
                "SELECT l " +
                    "FROM BestcutLike l " +
                    "WHERE l.bestcut.id = :bestcut AND l.member.id = :member"
                , BestcutLike.class)
                .setParameter("bestcut", bestcutId)
            .setParameter("member", memberId)
            .getResultList();

        return resultList.stream().findAny();
    }

    public void delete(BestcutLike bestcutLike) {
        em.remove(bestcutLike);
    }

}
