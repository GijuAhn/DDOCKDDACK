package com.ddockddack.domain.member.controller;


import com.ddockddack.domain.bestcut.service.BestcutService;
import com.ddockddack.domain.game.service.GameService;
import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.entity.Role;
import com.ddockddack.domain.member.request.MemberModifyReq;
import com.ddockddack.domain.member.response.MemberJoinRes;
import com.ddockddack.domain.member.response.MemberLoginPostRes;
import com.ddockddack.domain.member.service.MemberService;
//import com.ddockddack.domain.member.util.JwtTokenUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequiredArgsConstructor
@Tag(name="member", description = "member API 입니다.")
@RequestMapping("/members")
public class MemberApiController {

    private Environment env;
    private MemberService memberService;

    private BestcutService bestcutService;
    private GameService gameService;

    @Autowired
    public MemberApiController(Environment env, MemberService memberService, BestcutService bestcutService, GameService gameService) {
        this.env = env;
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
    public ResponseEntity<?> modifyMember(@PathVariable Long id, @RequestBody MemberModifyReq modifyMember){
        try{
            memberService.modifyMember(id, modifyMember);
            return ResponseEntity.ok("success 수정");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
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
    public ResponseEntity<?> getMemberInfo(@PathVariable Long memberId){
        try{
            Member member = memberService.getMemberById(memberId);
            return ResponseEntity.ok("Seccess 조회");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }

/*
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 타입 오류"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id, @PathVariable String memberId){
        try{
            Member member = memberService.deleteMemberById(id);
            return ResponseEntity.ok(MemberLoginPostRes.of(200, "Success", member, JwtTokenUtil.getToken(member.getEmail())));
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
    public ResponseEntity<?> getRecords(@PathVariable Long id){
        try{
            Member member = gameService.getRecordsById(id);
            return ResponseEntity.ok(MemberLoginPostRes.of(200, "Success", member, JwtTokenUtil.getToken(member.getEmail())));
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
    public ResponseEntity<?> getBestcuts(@PathVariable Long id){
        try{
            Member member = bestcutService.getBestcutsById(id); //member Response에 올려야 하나?
            return ResponseEntity.ok(MemberLoginPostRes.of(200, "Success", member, JwtTokenUtil.getToken(member.getEmail())));
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
    public ResponseEntity<?> getMyGames(@PathVariable Long id){
        try{
            Member member = memberService.getMyGamesById(id); //member Response에 올려야 하나?
            return ResponseEntity.ok(MemberLoginPostRes.of(200, "Success", member, JwtTokenUtil.getToken(member.getEmail())));
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
    public ResponseEntity<?> getGames(@PathVariable Long id){
        try{
            Member member = memberService.getGamesByStarred(id); //member Response에 올려야 하나?
            return ResponseEntity.ok(MemberLoginPostRes.of(200, "Success", member, JwtTokenUtil.getToken(member.getEmail())));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }
    }*/

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/kakaoLogin")
    public ResponseEntity<?> kakaoRequestAccessToken(@RequestParam String code) {
        //카카오 서버에 POST 방식으로 엑세스 토큰을 요청
        //RestTemplate를 이용
        try{
//            System.out.println(code);
            String accessToken = memberService.getKaKaoAccessToken(code);

            ResponseEntity<String> memberInfoResponse = memberService.getKakaoMember(accessToken);

            JsonParser jp = new JsonParser();
//            JsonObject jo = jp.parse(memberInfoResponse.getBody()).getAsJsonObject();

            JsonObject memberJsonObject = jp.parse(memberInfoResponse.getBody()).getAsJsonObject();
            JsonObject memberAccountObject = jp.parse(memberJsonObject.get("kakao_account").toString()).getAsJsonObject();
            String nickname = jp.parse(memberAccountObject.get("profile").toString()).getAsJsonObject().get("nickname").getAsString();
            System.out.println("nick");
            String email = memberAccountObject.get("email").getAsString();
            System.out.println("email");

//            String id = memberAccountObject.get("id").getAsString();

            System.out.println("=========================================");
            System.out.println("토큰에서 꺼내온 값 확인하기");
            System.out.println("닉네임 : " + nickname);
            System.out.println("닉네임 : " + nickname);
            System.out.println("email : " + email);

            System.out.println("================================================");
//            System.out.println(jp.parse(memberAccountObject.get("profile").toString()));

            Member member = memberService.getUserBySocialId(email);

            if(member == null){
                member = new Member(email, nickname, null, Role.member);
//                member.setNickname(nickname);
//                HttpHeaders tokenHeaders = new HttpHeaders();
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
                memberService.joinMember(member);

                return ResponseEntity.ok("Seccess join");
            }
            //else
            return ResponseEntity.ok("Seccess login");
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(500).body(e);
        }

        //토큰이 유효하지 않으면
//        return ResponseEntity.status(401).body(MemberLoginPostRes.of(401, "Invalid Token", null, null));

    }

    @Operation(summary = "구글 로그인", description = "구글 로그인 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/googleLogin")
    public ResponseEntity<?> GoogleRequestAccessToken(@RequestParam String code) {

        RestTemplate rt = new RestTemplate();


        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/oauth2/v4/token")
                .queryParam("code", code)
                .queryParam("client_id", "")
                .queryParam("client_secret", "")
                .queryParam("redirect_uri", "")
                .queryParam("grant_type", "authorization_code");

        HttpEntity request = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<String> response = rt.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                request,
                String.class
        );
        JsonParser jp = new JsonParser();
        JsonObject jo = jp.parse(response.getBody()).getAsJsonObject();
        String accessToken = jo.get("access_token").getAsString();

        HttpHeaders headers = new HttpHeaders();

        headers.add("authorization", "Bearer " + accessToken);

        HttpEntity memberInfoRequest = new HttpEntity<>(headers);


        ResponseEntity<String> responsememberInfo = rt.exchange(UriComponentsBuilder
                        .fromHttpUrl("https://www.googleapis.com/oauth2/v2/memberinfo")
                        .toUriString(),
                HttpMethod.GET,
                memberInfoRequest,
                String.class);


        return new ResponseEntity<>(responsememberInfo.getBody(), HttpStatus.OK);
    }
}
