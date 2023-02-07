package com.ddockddack.domain.gameRoom.controller;

import com.ddockddack.domain.gameRoom.response.GameRoomRes;
import com.ddockddack.domain.gameRoom.service.GameRoomService;
import com.ddockddack.global.service.AwsS3Service;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/game-rooms")
public class GameRoomApiController {

    private final GameRoomService gameRoomService;
    private final AwsS3Service awsS3Service;

    @PostMapping
    @Operation(summary = "게임방 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "방 생성 성공"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임")
    })
    public ResponseEntity<String> createRoom(@RequestBody Map<String, String> params) {
        String pinNumber;
        try {
            pinNumber = gameRoomService.createRoom(Long.parseLong(params.get("gameId")));
        } catch (OpenViduJavaClientException e) {
            throw new RuntimeException(e);
        } catch (OpenViduHttpException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(pinNumber, HttpStatus.OK);
    }

    @PostMapping("/{pinNumber}")
    @Operation(summary = "게임방 참가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "방 참가 성공"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임방")
    })
    public ResponseEntity<GameRoomRes> joinRoom(@PathVariable String pinNumber, @RequestHeader(value = "access-token", required = false) String accessToken, @RequestBody(required = false) String nickname) throws OpenViduJavaClientException, OpenViduHttpException {
        Long memberId = null;

        if (accessToken != null) {
            //로그인 한 경우 token에서 memberId 추출
            memberId = 1L;
        }
        return new ResponseEntity<>(gameRoomService.joinRoom(pinNumber, memberId, nickname), HttpStatus.OK);
    }

    @DeleteMapping("/{pinNumber}/sessions/{sessionId}")
    @Operation(summary = "게임방 멤버 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "방 나가기 성공"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임방")
    })
    public ResponseEntity removeGameMember(@PathVariable String pinNumber,
                                           @PathVariable String sessionId) {


        gameRoomService.removeGameMember(pinNumber, sessionId);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{pinNumber}")
    @Operation(summary = "게임방 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "방 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임방")
    })
    public ResponseEntity removeGameRoom(@PathVariable String pinNumber) {

        gameRoomService.removeGameRoom(pinNumber);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{pinNumber}")
    @Operation(summary = "게임시작")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게임 시작"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임방")
    })
    public ResponseEntity startGame(@PathVariable String pinNumber) {
        gameRoomService.startGame(pinNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{pinNumber}/isstarted")
    @Operation(summary = "게임 시작여부 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게임 시작여부 조회성공"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임방")
    })
    public ResponseEntity<Boolean> getIsStartedGame(@PathVariable String pinNumber) {

        return ResponseEntity.ok(gameRoomService.isStartedGame(pinNumber));
    }

    @PostMapping("/{pinNumber}/{sessionId}/images")
    @Operation(summary = "게임 멤버 이미지 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게임 멤버 이미지 저장성공"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임방")
    })
    public ResponseEntity scoringImage(@PathVariable("pinNumber") String pinNumber,
                                       @PathVariable("sessionId") String sessionId,
                                       @RequestBody HashMap<String, String> param) throws Exception {
        gameRoomService.scoringImage(pinNumber, sessionId, param);

        return ResponseEntity.ok().build();
    }

    @GetMapping("{pinNumber}/result/{round}")
    @Operation(summary = "게임 라운드 결과 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "라운드 결과 반환 성공")
    })
    public ResponseEntity getResult(@PathVariable("pinNumber") String pinNumber,
                                    @PathVariable("round") int round) {

        gameRoomService.findRoundResult(pinNumber, round);
        return ResponseEntity.ok().build();
    }

}