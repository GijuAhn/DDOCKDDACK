package com.ddockddack.domain.bestcut.request;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BestcutSaveReq {

    @NotNull
    private Long memberId;

    @NotBlank
    private String gameTitle;

    @Valid
    @Size(min = 1, max=10)
    @NotNull
    private List<BestcutImageReq> images;
}
