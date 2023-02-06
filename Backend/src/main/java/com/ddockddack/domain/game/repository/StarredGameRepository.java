package com.ddockddack.domain.game.repository;

import com.ddockddack.domain.game.entity.StarredGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StarredGameRepository extends JpaRepository<StarredGame, Long> {

    Optional<StarredGame> findByMemberIdAndGameId(Long memberId, Long gameId);

    boolean existsByMemberIdAndGameId(Long memberId, Long gameId);
}
