package com.ddockddack.domain.member.service;

import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.repository.MemberRepository;
import com.ddockddack.domain.member.request.MemberModifyReq;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional(readOnly = true)
//@RequiredArgsConstructor
public class MemberService { //ServiceImpl을 따로 만들어야 하나?
    private final Environment env;
    private final MemberRepository memberRepository;

    private RestTemplate rt;
    private HttpHeaders headers;

    @Autowired
    public MemberService(MemberRepository memberRepository, Environment env) {
        this.memberRepository = memberRepository;
        this.env = env;
    }

    @Transactional
    public Long joinMember(Member member) {
        memberRepository.save(member);

        return member.getId();
    }


    /**
     * @param memberId
     * @param modifyMember
     * @return 수정된 member
     */
    @Transactional
    public Member modifyMember(Long memberId, MemberModifyReq modifyMember) {
        Member memberToModify = memberRepository.findById(memberId).get();

        memberToModify.setNickname(modifyMember.getNickname());
        memberToModify.setProfile(modifyMember.getProfile());

        return memberRepository.save(memberToModify);
    }

    /**
     * @param memberId
     * @return member 정보
     */
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.getByEmail(email);
    }

    @Transactional
    public void deleteMemberById(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    /**
     * @param email
     * @return db에 입력된 email이 있는지
     */
    public boolean findUserBySocialId(String email) {
        return memberRepository.existsByEmail(email);
    }

//    public Member findOne(Long memberId) {
//        return memberRepository.findOne(Member);
//    }

    /**
     * @param code
     * @return Access 토큰
     */
    public String getKaKaoAccessToken(String code) {
        //카카오 서버에 POST 방식으로 엑세스 토큰을 요청

        rt = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", env.getProperty("kakao.api_key"));
        params.add("redirect_uri", env.getProperty("kakao.login.redirect_uri"));
        params.add("code", code);
        //HttpHeader와 HttpBody를 HttpEntity에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(params, headers);
        //카카오 서버에 HTTP 요청 - POST
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoRequest,
                String.class
        );

        System.out.println("response" + response);
        //응답에서 엑세스 토큰 받기
        JsonParser jp = new JsonParser();
        JsonObject jo = jp.parse(response.getBody()).getAsJsonObject();
        String accessToken = jo.get("access_token").getAsString();


        return accessToken;
    }

    /**
     * @param accessToken
     * @return 카카오 사용자 정보
     */
    public ResponseEntity<String> getKakaoMember(String accessToken) {
        //엑세스 토큰을 통해 사용자 정보를 응답 받기

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> memberInfoRequest = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> memberInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                memberInfoRequest,
                String.class
        );

        return memberInfoResponse;
    }

    public String getGoogleAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/oauth2/v4/token")
                .queryParam("code", code)
                .queryParam("client_id", env.getProperty("google.client_id"))
                .queryParam("client_secret", env.getProperty("google.client_secret"))
                .queryParam("redirect_uri", env.getProperty("google.login.redirect_uri"))
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

        return jo.get("access_token").getAsString();
    }

    public ResponseEntity<String> getGoogleMember(String accessToken) {
        rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("authorization", "Bearer " + accessToken);

        HttpEntity memberInfoRequest = new HttpEntity<>(headers);

        ResponseEntity<String> responsememberInfo = rt.exchange(UriComponentsBuilder
                        .fromHttpUrl("https://www.googleapis.com/oauth2/v2/userinfo")
                        .toUriString(),
                HttpMethod.GET,
                memberInfoRequest,
                String.class);

        return responsememberInfo;
    }

    public HttpStatus logoutKakao(Long memberId) {
        HttpHeaders headers = new HttpHeaders();

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", env.getProperty("kakao.api_key"));
        params.add("redirect_uri", env.getProperty("kakao.logout.redirect_uri"));
        //HttpHeader와 HttpBody를 HttpEntity에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(params, headers);
        //카카오 서버에 HTTP 요청 - POST
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/logout",
                HttpMethod.POST,
                kakaoRequest,
                String.class
        );

        return response.getStatusCode();
    }
}
