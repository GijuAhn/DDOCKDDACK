package com.ddockddack.domain.bestcut.controller;

import com.ddockddack.domain.bestcut.request.BestcutSaveReq;
import com.ddockddack.domain.bestcut.response.BestcutRes;
import com.ddockddack.domain.bestcut.service.BestcutLikeService;
import com.ddockddack.domain.bestcut.service.BestcutService;
import com.ddockddack.domain.report.entity.ReportType;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.AccessDeniedException;
import com.ddockddack.global.error.exception.NumberOfFileExceedException;
import com.ddockddack.global.util.PageConditionReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @DeleteMapping("/dislike/{bestcutId}")
    @Operation(summary = "베스트 컷 좋아요 취소")
    @ApiResponses({
            @ApiResponse(description = "베스트컷 좋아요 취소 성공", responseCode = "200"),
            @ApiResponse(description = "로그인 필요", responseCode = "401"),
            @ApiResponse(description = "존재하지 않는 베스트컷", responseCode = "404"),
            @ApiResponse(description = "존재하지 않는 멤버", responseCode = "404"),
    })
    public ResponseEntity bestcutDislike(@PathVariable Long bestcutId,
            @RequestHeader(value = "access-token", required = false) String accessToken) {
        checkLogin(accessToken);

        Long memberId = getMemberId(accessToken);
        bestcutLikeService.removeBestcutLike(bestcutId, memberId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/report/{bestcutId}")
    @Operation(summary = "베스트 컷 신고")
    @ApiResponses({
            @ApiResponse(description = "베스트컷 신고 성공", responseCode = "200"),
            @ApiResponse(description = "이미 신고한 베스트컷", responseCode = "400"),
            @ApiResponse(description = "로그인 필요", responseCode = "401"),
            @ApiResponse(description = "존재하지 않는 베스트컷", responseCode = "404"),
            @ApiResponse(description = "존재하지 않는 멤버", responseCode = "404"),
    })
    public ResponseEntity bescutReport(@PathVariable Long bestcutId,
            @RequestHeader(value = "access-token", required = false) String accessToken,
            @RequestBody Map<String, String> reportType) {
        checkLogin(accessToken);

        Long memberId = getMemberId(accessToken);
        bestcutService.reportBestcut(memberId, bestcutId,
                ReportType.valueOf(reportType.get("reportType")));

        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "베스트 컷 목록 조회")
    @ApiResponses({
            @ApiResponse(description = "베스트컷 조회 성공", responseCode = "200")
    })
    public ResponseEntity<PageImpl<BestcutRes>> bestcutList(@RequestHeader(value = "access-token", required = false) String accessToken, @ModelAttribute PageConditionReq pageConditionReq) {
        Long loginMemberId = getMemberId(accessToken);


        PageImpl<BestcutRes> result = bestcutService.findAll(false, loginMemberId, pageConditionReq);

        return ResponseEntity.ok(result);
    }

    @GetMapping("{bestcutId}")
    @Operation(summary = "베스트 컷 단건 조회")
    @ApiResponses({
            @ApiResponse(description = "베스트컷 조회 성공", responseCode = "200"),
            @ApiResponse(description = "존재하지 않는 베스트컷", responseCode = "404")
    })
    public ResponseEntity<BestcutRes> bestcutFind(@RequestHeader(value = "access-token", required = false) String accessToken, @PathVariable Long bestcutId){
        Long loginMemberId = getMemberId(accessToken);
        BestcutRes findBestcut = bestcutService.findOne(loginMemberId, bestcutId);

        return ResponseEntity.ok(findBestcut);
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
