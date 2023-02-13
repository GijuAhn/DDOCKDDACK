package com.ddockddack.domain.gameRoom.repository;

import com.ddockddack.domain.gameRoom.entity.GameRoomHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRoomHistoryRepository extends JpaRepository<GameRoomHistory, Long> {

    List<GameRoomHistory> findByMemberId(Long memberId);
}
