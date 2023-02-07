package com.ddockddack.domain.admin.controller;

import com.ddockddack.domain.bestcut.response.BestcutRes;
import com.ddockddack.domain.bestcut.service.BestcutService;
import com.ddockddack.domain.game.response.ReportedGameRes;
import com.ddockddack.domain.game.service.GameService;
import com.ddockddack.domain.member.controller.TokenController;
import com.ddockddack.domain.member.service.MemberService;
import com.ddockddack.domain.member.service.TokenService;
import com.ddockddack.domain.report.entity.ReportedBestcut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminApiController {

    private final BestcutService bestcutService;

    private final GameService gameService;

    private final TokenService tokenService;
    private final MemberService memberService;

    @GetMapping()
    @Operation(summary = "신고된 게임 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신고된 게임 목록 조회 성공"),
            @ApiResponse(responseCode = "403", description = "허가되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> reportedGameList(@RequestHeader(value = "access-token", required = true) String accessToken) {

        if(!memberService.isAdmin(tokenService.getUid(accessToken))){
            return ResponseEntity.status(403).body("");
        }

        try {
            List<ReportedGameRes> allReportedGames = gameService.findAllReportedGames();
            return ResponseEntity.ok(allReportedGames);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }

    }
    @GetMapping()
    @Operation(summary = "신고된 베스트 컷 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신고된 베스트 컷 목록 조회 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> reportedBestCutList(@RequestHeader(value = "access-token", required = true) String accessToken) {

        if(!memberService.isAdmin(tokenService.getUid(accessToken))){
            return ResponseEntity.status(403).body("");
        }

        try {
            List<ReportedBestcut> allReportedBestCuts = bestcutService.findAllReportedBestCuts();
            return ResponseEntity.ok(allReportedBestCuts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }

    }
}
