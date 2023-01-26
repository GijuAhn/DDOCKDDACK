package com.ddockddack.domain.bestcut.service;

import com.ddockddack.domain.bestcut.entity.Bestcut;
import com.ddockddack.domain.bestcut.repository.BestcutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BestcutService {
    private final BestcutRepository bestcutRepository;

    @Autowired
    public BestcutService(BestcutRepository bestcutRepository) {
        this.bestcutRepository = bestcutRepository;
    }

    public List<Bestcut> getBestcutsById(Long memberId) {
        return bestcutRepository.findByMemberId(memberId);
    }
}
