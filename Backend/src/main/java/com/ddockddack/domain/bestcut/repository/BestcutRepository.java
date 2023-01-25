package com.ddockddack.domain.bestcut.repository;

import com.ddockddack.domain.bestcut.entity.Bestcut;
import com.ddockddack.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BestcutRepository extends JpaRepository<Member, Long> {
    public List<Bestcut> findByMemberId(Long memberId);
}
