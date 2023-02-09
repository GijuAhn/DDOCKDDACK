package com.ddockddack.domain.admin.controller;

import com.ddockddack.domain.admin.service.AdminService;
import com.ddockddack.domain.bestcut.repository.BestcutRepositorySupport;
import com.ddockddack.domain.bestcut.response.ReportedBestcutRes;
import com.ddockddack.domain.bestcut.service.BestcutService;
import com.ddockddack.domain.game.repository.GameRepository;
import com.ddockddack.domain.game.repository.GameRepositorySupport;
import com.ddockddack.domain.game.response.ReportedGameRes;
import com.ddockddack.domain.game.service.GameService;
import com.ddockddack.domain.member.request.MemberModifyReq;
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

import java.time.LocalDate;
import java.util.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminApiController {

    private final BestcutService bestcutService;
    private final GameService gameService;
    private final AdminService adminService;
    private final TokenService tokenService;
    private final MemberService memberService;
    private final BestcutRepositorySupport bestcutRepositorySupport;
    private final GameRepositorySupport gameRepositorySupport;

    @GetMapping("/reported/games")
    @Operation(summary = "신고된 게임 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "신고된 게임 목록 조회 성공"),
            @ApiResponse(responseCode = "403", description = "허가되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> reportedGameList(@RequestHeader(value = "access-token", required = true) String accessToken) {
        Long adminId = tokenService.getUid(accessToken);

        // admin 확인
        if(!adminService.isAdminByAccessToken(adminId)){
            return ResponseEntity.status(403).body(null);
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
        Long adminId = tokenService.getUid(accessToken);

        // admin 확인
        if(!adminService.isAdminByAccessToken(adminId)){
            return ResponseEntity.status(403).body(null);
        }

        try {
            List<ReportedBestcutRes> allReportedBestCuts = bestcutService.findAllReportedBestCuts();
            return ResponseEntity.ok(allReportedBestCuts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }

    }

    @DeleteMapping("/remove/game/{reportId}/{gameId}")
    @Operation(summary = "신고된 게임 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게임 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "403", description = "허가되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 게임"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 유저")
    })
    public ResponseEntity reportedGameDelete(@PathVariable Long gameId, @PathVariable Long reportId,
                                             @RequestHeader(value = "access-token", required = true) String accessToken,
                                             @RequestBody int[] releaseDate) {
        Long adminId = tokenService.getUid(accessToken);

        // admin 확인
        if(!adminService.isAdminByAccessToken(adminId)){
            return ResponseEntity.status(403).body(null);
        }

        gameService.removeGame(adminId, gameId);
        memberService.banMember(reportId, LocalDate.of(releaseDate[0], releaseDate[1], releaseDate[2]));
        gameRepositorySupport.removeReportedGame(reportId);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/remove/game/{reportId}")
    @Operation(summary = "게임 신고 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게임 신고 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "403", description = "허가되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 신고"),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 유저")
    })
    public ResponseEntity gameReportDelete(@PathVariable Long reportId,
                                           @RequestHeader(value = "access-token", required = true) String accessToken,
                                           @RequestBody int[] releaseDate) {

        Long adminId = tokenService.getUid(accessToken);

        // admin 확인
        if(!adminService.isAdminByAccessToken(adminId)){
            return ResponseEntity.status(403).body(null);
        }
        gameRepositorySupport.removeReportedGame(reportId);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/remove/bestcut/{reportId}/{bestcutId}")
    @Operation(summary = "신고된 베스트 컷 삭제")
    @ApiResponses({
            @ApiResponse(description = "베스트컷 삭제 성공", responseCode = "200"),
            @ApiResponse(description = "권한 없음", responseCode = "401"),
            @ApiResponse(responseCode = "403", description = "허가되지 않은 사용자"),
            @ApiResponse(description = "존재하지 않는 베스트컷", responseCode = "404"),
            @ApiResponse(description = "존재하지 않는 멤버", responseCode = "404"),
    })
    public ResponseEntity reportedBestCutDelete(@PathVariable Long bestcutId, @PathVariable Long reportId,
                                                @RequestHeader(value = "access-token", required = true) String accessToken,
                                                @RequestBody int[] releaseDate) {
        Long adminId = tokenService.getUid(accessToken);

        // admin 확인
        if(!adminService.isAdminByAccessToken(adminId)){
            return ResponseEntity.status(403).body(null);
        }

        bestcutService.removeBestcut(bestcutId, adminId);
        memberService.banMember(reportId, LocalDate.of(releaseDate[0], releaseDate[1], releaseDate[2]));
        bestcutRepositorySupport.removeReportedBestcut(reportId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정지", description = "회원 정지 메소드입니다.")
    @PutMapping("/ban/{memberId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력 조회 성공"),
            @ApiResponse(responseCode = "403", description = "허가되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> banMember(@PathVariable Long memberId,
                                       @RequestBody MemberModifyReq modifyMemberReq,
                                       @RequestBody int[] releaseDate,
                                       @RequestHeader(value = "access-token", required = true) String accessToken) {

        Long adminId = tokenService.getUid(accessToken);

        // admin 확인
        if(!adminService.isAdminByAccessToken(adminId)){
            return ResponseEntity.status(403).body(null);
        }

        try {
            memberService.banMember(memberId, LocalDate.of(releaseDate[0], releaseDate[1], releaseDate[2]));
            return ResponseEntity.ok(memberService.modifyMember(memberId, modifyMemberReq));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
