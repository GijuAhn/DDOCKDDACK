package com.ddockddack.domain.bestcut.controller;

import com.ddockddack.domain.bestcut.entity.Bestcut;
import com.ddockddack.domain.bestcut.repository.BestcutRepository;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.entity.Role;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.NotFoundException;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
class BestcutApiControllerTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BestcutRepository bestcutRepository;


    @Test
    @DisplayName("repository: DB에 베스트컷이 잘 저장되는지 확인")
    public void 베스트컷_저장() throws Exception {
        //given
        Bestcut stubBestcut = getStubBestcut();

        //when
        Long savedId = bestcutRepository.save(stubBestcut);
        Bestcut findBestcut = bestcutRepository.findById(savedId).get();

        //then
        Assertions.assertThat(stubBestcut).isSameAs(findBestcut);
        Assertions.assertThat(stubBestcut.getId()).isEqualTo(savedId);
        Assertions.assertThat(stubBestcut.getMember()).isSameAs(findBestcut.getMember());
        Assertions.assertThat(stubBestcut.getTitle()).isSameAs(findBestcut.getTitle());
    }

    @Test
    @DisplayName("repository: DB에서 베스트컷이 삭제되는지 확인")
    public void 베스트컷_삭제() {
        //given
        Bestcut stubBestcut = getStubBestcut();

        //when
        Long savedId = bestcutRepository.save(stubBestcut);
        bestcutRepository.delete(stubBestcut);

        //then
        Assertions.assertThatThrownBy(() -> bestcutRepository.findById(savedId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.BESTCUT_NOT_FOUND)))
                .isInstanceOf(NotFoundException.class);
    }


    private Bestcut getStubBestcut() {
        Member member = new Member("test@test.com", "test", "https://test/test.png", Role.MEMBER);
        testEntityManager.persist(member);

        Bestcut bestcut = new Bestcut(member, "s3:test", "testGameTitle", "testGameDesc",
                "testBestcutTitle", "https://test/test.png");
        ReflectionTestUtils.setField(bestcut, "createdAt", LocalDateTime.now());

        return bestcut;
    }
}