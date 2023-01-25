package com.ddockddack.domain.member.repository;

import com.ddockddack.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public Member findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email limit 1", Member.class)
            .setParameter("email", email)
                .getSingleResult();

    }

//    public Member getBySocialId(String email) {return }
}
