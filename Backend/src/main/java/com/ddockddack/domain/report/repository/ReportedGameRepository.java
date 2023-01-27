package com.ddockddack.domain.report.repository;

import com.ddockddack.domain.report.entity.ReportedGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedGameRepository extends JpaRepository<ReportedGame, Long> {

    boolean existsByReportMemberIdAndGameId(Long memberId, Long gameId);

}
