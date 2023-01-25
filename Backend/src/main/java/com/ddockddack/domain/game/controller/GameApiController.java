package com.ddockddack.domain.game.controller;

import com.ddockddack.domain.game.request.GameSaveReq;
import com.ddockddack.domain.game.response.GameRes;
import com.ddockddack.domain.game.service.GameService;
import com.ddockddack.global.util.OrderCondition;
import com.ddockddack.global.util.PeriodCondition;
import com.ddockddack.global.util.SearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameApiController {

    private final GameService gameService;

    @GetMapping()
    @Operation(summary = "게임 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게임 목록 조회 성공")
    })
    public ResponseEntity<List<GameRes>> gameList(@RequestParam(required = false) OrderCondition orderCondition,
                                                  @RequestParam(required = false) String period,
                                                  @RequestParam(required = false) SearchCondition searchCondition,
                                                  @RequestParam(required = false) String keyword,
                                                  @RequestHeader(value = "access-token", required = false) String accessToken) {

        PeriodCondition periodCondition =
                (period == null || period.isBlank()) ? null : PeriodCondition.valueOf(period);

        List<GameRes> allBySearch =
                gameService.findAllGames(orderCondition, periodCondition, searchCondition, keyword, null);

        return ResponseEntity.ok(allBySearch);
    }

    @PostMapping
    @Operation(summary = "게임 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게임 생성 성공"),
            @ApiResponse(responseCode = "400", description = "필수 값 누락"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 유저"),
            @ApiResponse(responseCode = "413", description = "파일 용량 초과"),
            @ApiResponse(responseCode = "414", description = "입력 범위 벗어남"),
            @ApiResponse(responseCode = "414", description = "지원 하지 않는 확장자")
    })
    public ResponseEntity gameSave(@ModelAttribute @Valid GameSaveReq gameSaveReq) {

        gameService.saveGame(gameSaveReq);
        return ResponseEntity.ok().build();

    }

}
