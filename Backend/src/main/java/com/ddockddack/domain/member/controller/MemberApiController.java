package com.ddockddack.domain.member.controller;


import com.ddockddack.domain.bestcut.entity.Bestcut;
import com.ddockddack.domain.bestcut.service.BestcutService;
import com.ddockddack.domain.game.service.GameService;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.entity.Role;
import com.ddockddack.domain.member.request.MemberModifyReq;
import com.ddockddack.domain.member.response.MemberRes;
import com.ddockddack.domain.member.service.MemberService;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.AccessDeniedException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "member", description = "member API 입니다.")
@RequestMapping("/members")
public class MemberApiController {

    private MemberService memberService;
    private BestcutService bestcutService;
    private GameService gameService;

    @Autowired
    public MemberApiController(MemberService memberService, BestcutService bestcutService, GameService gameService) {
        this.memberService = memberService;
        this.bestcutService = bestcutService;
        this.gameService = gameService;
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력 조회 성공"),
            @ApiResponse(responseCode = "400", description = "필수 값 누락"),
            @ApiResponse(responseCode = "400", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "413", description = "파일용량 초과"),
            @ApiResponse(responseCode = "415", description = "지원하지않는 확장자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{memberId}")
    public ResponseEntity<?> modifyMember(@PathVariable Long memberId, @RequestBody MemberModifyReq modifyMemberReq,
                                          @RequestHeader(value = "access-token", required = false) String accessToken) {
        try {
//            memberService.modifyMember(memberId, modifyMember);
            return ResponseEntity.ok(memberService.modifyMember(memberId, modifyMemberReq));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Operation(summary = "내 정보 조회", description = "회원 정보 조회 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 타입 오류"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getMemberInfo(@PathVariable Long memberId) {
        try {
            Member member = memberService.getMemberById(memberId);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }

    @Operation(summary = "내 베스트 컷 전체 조회", description = "내 베스트 컷 전체 조회 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 베스트 컷 전체 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 타입 오류"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{memberId}/bestcuts")
    public ResponseEntity<?> getBestcuts(@PathVariable Long memberId,
                                         @RequestHeader(value = "access-token", required = false) String accessToken) {
        try {
//            List<Bestcut> bestcuts = bestcutService.getBestcutsById(memberId);

            return ResponseEntity.ok("success 베스트컷조회");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 타입 오류"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId,
                                          @RequestHeader(value = "access-token", required = false) String accessToken) {
        try {
            memberService.deleteMemberById(memberId);
            return ResponseEntity.ok("success 삭제");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }

    @Operation(summary = "내가 만든 게임 전체 조회", description = "내가 만든 게임 전체 조회 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 만든 게임 전체 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 타입 오류"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{memberId}/games")
    public ResponseEntity<?> getMyGames(@PathVariable Long memberId,
                                        @RequestHeader(value = "access-token", required = false) String accessToken) {
        try {
//            Member member = gameService.getMyGamesByMemberId(memberId); //member Response에 올려야 하나?
            return ResponseEntity.ok("MemberRes");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }

    @Operation(summary = "즐겨찾기 게임 전체 조회", description = "내가 만든 게임 전체 조회 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "즐겨찾기 게임 전체 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 타입 오류"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{memberId}/starred")
    public ResponseEntity<?> getGames(@PathVariable Long memberId,
                                      @RequestHeader(value = "access-token", required = false) String accessToken) {
        try {
//            Member member = gameService.getGamesByStarred(memberId); //member Response에 올려야 하나?
            return ResponseEntity.ok("즐겨찾기 조회 성공");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }

    @Operation(summary = "게임 이력 조회", description = "게임 이력 조회 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 타입 오류"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{memberId}/records")
    public ResponseEntity<?> getRecords(@PathVariable Long memberId,
                                        @RequestHeader(value = "access-token", required = false) String accessToken) {
        try {
//            Member member = gameService.getRecordsById(memberId);
            return ResponseEntity.ok("게임 이력 조회 성공");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/kakaologin")
    public ResponseEntity<?> kakaoRequestAccessToken(@RequestParam String code) {
        //카카오 서버에 POST 방식으로 엑세스 토큰을 요청
        //RestTemplate를 이용
        try {
            System.out.println(code);
            String accessToken = memberService.getKaKaoAccessToken(code);

            ResponseEntity<String> memberInfoResponse = memberService.getKakaoMember(accessToken);

            JsonParser jp = new JsonParser();
//            JsonObject jo = jp.parse(memberInfoResponse.getBody()).getAsJsonObject();

            JsonObject memberJsonObject = jp.parse(memberInfoResponse.getBody()).getAsJsonObject();
            JsonObject memberAccountObject = jp.parse(memberJsonObject.get("kakao_account").toString()).getAsJsonObject();
            String nickname = jp.parse(memberAccountObject.get("profile").toString()).getAsJsonObject().get("nickname").getAsString();
            String email = memberAccountObject.get("email").getAsString();

            boolean isMember = memberService.findUserBySocialId(email);

            if (!isMember) {
                Member member = new Member(email, nickname, "", Role.MEMBER);
                MemberRes memberRes = new MemberRes(member.getNickname(), member.getProfile());

                memberService.joinMember(member);

                return ResponseEntity.ok(memberRes);
            }
            //else
            return ResponseEntity.ok(memberService.getMemberByEmail(email));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(e);
        }

    }

    @Operation(summary = "구글 로그인", description = "구글 로그인 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/googlelogin")
    public ResponseEntity<?> GoogleRequestAccessToken(@RequestParam String code) {
        try{
            String accessToken = memberService.getGoogleAccessToken(code);

            ResponseEntity<String> memberInfoResponse = memberService.getGoogleMember(accessToken);

            JsonParser jp = new JsonParser();

            JsonObject memberJsonObject = jp.parse(memberInfoResponse.getBody()).getAsJsonObject();

            String email = memberJsonObject.get("email").getAsString();
            boolean isMember = memberService.findUserBySocialId(email);

            if (!isMember) {
                Member member = new Member(email, "", "", Role.member);
                MemberRes memberRes = new MemberRes(member.getNickname(), member.getProfile());

                memberService.joinMember(member);

                return ResponseEntity.ok(memberRes);
            }
            //else
            return ResponseEntity.ok(memberService.getMemberByEmail(email));

        } catch(Exception e) {
            throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZED);
        }
    }


    @Operation(summary = "로그아웃", description = "로그아웃 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@PathVariable Long memberId,
                                    @RequestHeader(value = "access-token", required = false) String accessToken) {
        try{
            String loginType = "kakao"; //로그인 타입 확인

            if (loginType.equals("kakao")) { //가입되어있지 않은 member면 로그인 정보로 회원가입
                return ResponseEntity.ok(memberService.logoutKakao(memberId));
            } else if (loginType.equals("google")){
                //구글 로그아웃
            } else {
                throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZED);
            }
        } catch(Exception e) {
            throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZED);
        }

        return null;
    }

    private void checkLogin(String accessToken) {
        if (accessToken == null) {
            throw new AccessDeniedException(ErrorCode.LOGIN_REQUIRED); //권한이 없는 데이터 접속
        }
    }

    private void unlinkKakao() {
        //                HttpHeaders tokenHeaders = new HttpHeaders(); //로그아웃하면 accessToken 연결 끊기
//                tokenHeaders.add("Authorization", "Bearer " + accessToken);
//                tokenHeaders.add("Content-type", "application/x-www-form-urlencoded");
//
//                HttpEntity<MultiValueMap<String, String>> memberUnlinkReq = new HttpEntity<>(tokenHeaders);
//
//                RestTemplate rt = new RestTemplate();
//
//                ResponseEntity<String> memberUnlinkRes = rt.exchange(
//                        "https://kapi.kakao.com/v1/member/unlink",
//                        HttpMethod.POST,
//                        memSberUnlinkReq,
//                        String.class
//                );

//                System.out.println("=============================");
//                System.out.println("유저 해제" + memberUnlinkRes);
    }

}
