package com.ddockddack.domain.game.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class GameModifyReq {

    @NotNull(message = "gameId is not null.")
    private Long gameId;

    private String gameTitle;

    private String gameDesc;

    private List<GameImageModifyReq> images;
}
