package com.ddockddack.domain.bestcut.repository;

import com.ddockddack.domain.bestcut.entity.Bestcut;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BestcutRepository {
    private final EntityManager em;

    public Long save(Bestcut bestcut) {
        em.persist(bestcut);
        return bestcut.getId();
    }

    public Optional<Bestcut> findById(Long bestcutId) {
        return Optional.ofNullable(em.find(Bestcut.class, bestcutId));
    }
}
