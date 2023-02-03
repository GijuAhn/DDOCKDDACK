package com.ddockddack.member;

import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.entity.Role;
import com.ddockddack.domain.member.repository.MemberRepository;
import com.ddockddack.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class Service {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception {
        //given 주어지면
//        Member member = new Member("test@naver.com", "kim", "", Role.MEMBER);
//        member.setEmail("test@naver.com");
//        member.setNickname("kim");

        //when 이렇게하면
//        Long saveId = memberService.joinMember(member);

        //then //이렇게 검증됨
//        em.flush();
//        assertEquals(member, memberRepository.getReferenceById(saveId));
    }

//    @Test
//    public void 중복_회원_예외() throws Exception {
//        //given
//        Member member1 = new Member();
//        member1.setName("kim");
//
//        Member member2 = new Member();
//        member2.setName("kim");
//
//        //when
//        memberService.join(member1);
////        memberService.join(member2); //예외가 발생한다!!
//
//        assertThrows(IllegalStateException.class, () -> {
//            memberService.join(member2);
//        });
//
//        //then
////        fail("예외가 발생해야 합니다.");
//
//    }
}
