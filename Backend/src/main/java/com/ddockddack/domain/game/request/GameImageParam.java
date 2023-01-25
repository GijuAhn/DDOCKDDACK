package com.ddockddack.domain.game.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class GameImageParam {

    @NotNull(message = "gameImage cannot be empty.")
    private MultipartFile gameImage;

    @Size(max = 50, message = "The maximum length of the gameImageDesc is 50.")
    private String gameImageDesc;

}
