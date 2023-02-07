package com.ddockddack.domain.admin.controller;

import com.ddockddack.domain.admin.service.AdminService;
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
    private final AdminService adminService;

    @GetMapping("/reported/games")
    @Operation(summary = "신고된 게임 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신고된 게임 목록 조회 성공"),
            @ApiResponse(responseCode = "403", description = "허가되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> reportedGameList(@RequestHeader(value = "access-token", required = true) String accessToken) {

        // admin 확인
        if(!adminService.isAdminByAccessToken(accessToken)){
            return ResponseEntity.status(403).body("");
        }

        try {
            List<ReportedGameRes> allReportedGames = gameService.findAllReportedGames();
            return ResponseEntity.ok(allReportedGames);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }

    }
    @GetMapping("/reported/bestcuts")
    @Operation(summary = "신고된 베스트 컷 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신고된 베스트 컷 목록 조회 조회 성공"),
            @ApiResponse(responseCode = "403", description = "허가되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> reportedBestCutList(@RequestHeader(value = "access-token", required = true) String accessToken) {

        // admin 확인
        if(!adminService.isAdminByAccessToken(accessToken)){
            return ResponseEntity.status(403).body("");
        }

        try {
            List<ReportedBestcut> allReportedBestCuts = bestcutService.findAllReportedBestCuts();
            return ResponseEntity.ok(allReportedBestCuts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }

    }

    @DeleteMapping("/remove/game/{gameId}")
    @Operation(summary = "신고된 게임 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게임 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "403", description = "허가되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 유저")
    })
    public ResponseEntity gameRemove(@PathVariable Long gameId,
                                     @RequestHeader(value = "access-token", required = true) String accessToken) {

        // admin 확인
        if(!adminService.isAdminByAccessToken(accessToken)){
            return ResponseEntity.status(403).body("");
        }

        gameService.removeGame(1L, gameId);

        return ResponseEntity.ok().build();

    }
}
