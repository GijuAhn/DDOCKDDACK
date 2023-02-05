package com.ddockddack.domain.admin.controller;

import com.ddockddack.domain.bestcut.service.BestcutService;
import com.ddockddack.domain.game.response.ReportedGameRes;
import com.ddockddack.domain.game.service.GameService;
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

    @GetMapping()
    @Operation(summary = "신고된 게임 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신고된 게임 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> reportedGameList(@RequestHeader(value = "access-token", required = false) String accessToken) {

        try {
            List<ReportedGameRes> allReportedGames = gameService.findAllReportedGames();
            return ResponseEntity.ok(allReportedGames);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }

    }

}
