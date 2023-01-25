package com.ddockddack.domain.member.repository;

import com.ddockddack.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

//    private final EntityManager em;

//    public void save(Member member) {
//        em.persist(member);
//    }

    Member getReferenceById (Long id);

    public Member findByEmail(String email);



//    public Member getBySocialId(String email) {return }
}
