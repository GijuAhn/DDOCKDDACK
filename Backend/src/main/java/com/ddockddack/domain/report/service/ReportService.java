package com.ddockddack.domain.report.service;

import com.ddockddack.domain.report.repository.ReportedGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportedGameRepository reportedGameRepository;

    /**
     * 신고된 게임 삭제
     *
     * @param reportId
     */
    public void removeReportedGame(Long reportId) {
        reportedGameRepository.deleteById(reportId);
    }
}
