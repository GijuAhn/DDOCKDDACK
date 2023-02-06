package com.ddockddack.domain.game.repository;

import com.ddockddack.domain.game.entity.GameImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameImageRepository extends JpaRepository<GameImage, Long> {

}
