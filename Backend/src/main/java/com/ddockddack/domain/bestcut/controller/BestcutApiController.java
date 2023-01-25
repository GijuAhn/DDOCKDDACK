package com.ddockddack.domain.bestcut.controller;

import com.ddockddack.domain.bestcut.request.BestcutSaveReq;
import com.ddockddack.domain.bestcut.service.BestcutLikeService;
import com.ddockddack.domain.bestcut.service.BestcutService;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.AccessDeniedException;
import com.ddockddack.global.error.exception.NumberOfFileExceedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bestcuts")
public class BestcutApiController {
    private final BestcutService bestcutService;
    private final BestcutLikeService bestcutLikeService;


    @PostMapping
    @Operation(summary = "베스트 컷 게시")
    @ApiResponses({
            @ApiResponse(description = "베스트컷 게시 성공", responseCode = "200"),
            @ApiResponse(description = "필수 값 누락", responseCode = "400"),
            @ApiResponse(description = "로그인 필요", responseCode = "401"),
    })
    public ResponseEntity bestcutSave(@ModelAttribute @Valid BestcutSaveReq saveReq,
            @RequestHeader(value = "access-token", required = false) String accessToken) {
        if (saveReq.getImages().size() > 10) {
            throw new NumberOfFileExceedException(ErrorCode.EXCEED_FILE_NUMBER);
        }

        checkLogin(accessToken);

        if(getMemberId(accessToken) != saveReq.getMemberId()){
            throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZED);
        }

        bestcutService.saveBestcut(saveReq);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bestcutId}")
    @Operation(summary = "베스트 컷 삭제")
    @ApiResponses({
            @ApiResponse(description = "베스트컷 삭제 성공", responseCode = "200"),
            @ApiResponse(description = "권한 없음", responseCode = "401"),
            @ApiResponse(description = "존재하지 않는 베스트컷", responseCode = "404"),
            @ApiResponse(description = "존재하지 않는 멤버", responseCode = "404"),
    })
    public ResponseEntity bestcutDelete(@PathVariable Long bestcutId,
            @RequestHeader(value = "access-token", required = false) String accessToken) {
        Long memberId = getMemberId(accessToken);
        bestcutService.removeBestcut(bestcutId, memberId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/like/{bestcutId}")
    @Operation(summary = "베스트 컷 좋아요")
    @ApiResponses({
            @ApiResponse(description = "베스트컷 좋아요 성공", responseCode = "200"),
            @ApiResponse(description = "이미 좋아요한 베스트컷", responseCode = "400"),
            @ApiResponse(description = "로그인 필요", responseCode = "401"),
            @ApiResponse(description = "존재하지 않는 베스트컷", responseCode = "404"),
            @ApiResponse(description = "존재하지 않는 멤버", responseCode = "404"),
    })
    public ResponseEntity bestcutLike(@PathVariable Long bestcutId,
            @RequestHeader(value = "access-token", required = false) String accessToken) {
        checkLogin(accessToken);

        Long memberId = getMemberId(accessToken);
        bestcutLikeService.saveBestcutLike(bestcutId, memberId);

        return ResponseEntity.ok().build();
    }

    //accessToken에서 memberId 추출 코드 필요
    private Long getMemberId(String accessToken) {
        if (accessToken == null) {
            return 0L;
        }
        return 1L;
    }

    private void checkLogin(String accessToken) {
        if (accessToken == null) {
            throw new AccessDeniedException(ErrorCode.LOGIN_REQUIRED);
        }
    }
}
