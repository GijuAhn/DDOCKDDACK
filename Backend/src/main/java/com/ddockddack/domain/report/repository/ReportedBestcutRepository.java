package com.ddockddack.domain.report.repository;

import com.ddockddack.domain.game.response.QReportedGameRes;
import com.ddockddack.domain.game.response.ReportedGameRes;
import com.ddockddack.domain.report.entity.ReportedBestcut;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import com.ddockddack.domain.report.entity.ReportedGame;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static com.ddockddack.domain.member.entity.QMember.member;
import static com.ddockddack.domain.report.entity.QReportedGame.reportedGame;

@Repository
public interface ReportedBestcutRepository extends JpaRepository<ReportedBestcut, Long> {
    boolean existsByReportMemberIdAndBestcutId(Long memberId, Long bestcutId);
}
