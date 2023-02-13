package com.ddockddack.domain.gameRoom.repository;

import com.ddockddack.domain.gameRoom.entity.GameRoomHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;

public interface GameRoomHistoryRepository extends JpaRepository<GameRoomHistory, Long> {
    List<GameRoomHistory> findByMemberId(Long memberId);
}
