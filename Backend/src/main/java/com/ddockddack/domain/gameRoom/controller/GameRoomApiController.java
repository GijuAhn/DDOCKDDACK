package com.ddockddack.domain.gameRoom.controller;

import com.ddockddack.domain.gameRoom.response.GameRoomRes;
import com.ddockddack.domain.gameRoom.service.GameRoomService;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/game-rooms")
public class GameRoomApiController {

    private final GameRoomService gameRoomService;

    @PostMapping
    @Operation(summary = "게임방 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "방 생성 성공"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임")
    })
    public ResponseEntity<GameRoomRes> createRoom(@RequestBody Map<String, String> params) {
        GameRoomRes createdRoom;
        try {
            createdRoom = gameRoomService.createRoom(Long.parseLong(params.get("gameId")));
        } catch (OpenViduJavaClientException e) {
            throw new RuntimeException(e);
        } catch (OpenViduHttpException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(createdRoom, HttpStatus.OK);
    }

    @PostMapping("/{pinNumber}")
    @Operation(summary = "게임방 참가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "방 참가 성공"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임방")
    })
    public ResponseEntity<String> joinRoom(@PathVariable String pinNumber, @RequestHeader(value = "access-token", required = false) String accessToken, @RequestBody(required = false) String nickname) throws OpenViduJavaClientException, OpenViduHttpException {
        Long memberId = null;

        if (accessToken != null) {
            //로그인 한 경우 token에서 memberId 추출
            memberId = 1L;
        }

        String token = gameRoomService.joinRoom(pinNumber, memberId, nickname);

        return new ResponseEntity<>(token, HttpStatus.OK);
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
    public ResponseEntity saveMemberGameImage(@PathVariable("pinNumber") String pinNumber,
                                              @PathVariable("sessionId") String sessionId,
                                              @RequestBody HashMap<String, String> param) throws IOException {

        gameRoomService.saveGameMemberImage(pinNumber, sessionId, param.get("memberGameImage"));

        return ResponseEntity.ok().build();
    }

}