package com.ddockddack.domain.bestcut.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BestcutImageReq {
    @NotNull
    private MultipartFile bestcutImg;

    @NotBlank
    private String bestcutImgTitle;

    @NotBlank
    private String gameImgUrl;

    @NotBlank
    private String gameImgDesc;
}
