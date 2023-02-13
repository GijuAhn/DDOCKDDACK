package com.ddockddack.domain.game.repository;

import com.ddockddack.domain.game.entity.GameImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameImageRepository extends JpaRepository<GameImage, Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM GameImage gi WHERE gi.game.id = :id")
    void deleteByGameId(Long id);
}
