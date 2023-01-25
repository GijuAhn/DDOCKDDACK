package com.ddockddack.domain.member.service;

import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.repository.MemberRepository;
import com.ddockddack.domain.member.request.MemberModifyReq;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
//@RequiredArgsConstructor
public class MemberService { //ServiceImpl을 따로 만들어야 하나?


    private Environment env;
    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, Environment env) {
        this.memberRepository = memberRepository;
        this.env = env;
    }



    @Transactional
    public Long joinMember(Member member){
        memberRepository.save(member);

        return member.getId();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public Member getUserBySocialId(String email) {
        Member member = memberRepository.findByEmail(email);

        return member;
    }


    public String getKaKaoAccessToken(String code) {
        //카카오 서버에 POST 방식으로 엑세스 토큰을 요청
        //RestTemplate를 이용
        System.out.println(code + " ############");
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();

        System.out.println("인가 코드 확인 :" + code);
        System.out.println(env.getProperty("kakao.login.api_key"));

        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", env.getProperty("kakao.login.api_key"));
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

    public ResponseEntity<String> getKakaoMember(String accessToken) {
        //엑세스 토큰을 통해 사용자 정보를 응답 받기

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.add("Authorization", "Bearer " + accessToken);
        tokenHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> memberInfoRequest = new HttpEntity<>(tokenHeaders);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> memberInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                memberInfoRequest,
                String.class
        );

        System.out.println("userinfo " + memberInfoResponse);

        return memberInfoResponse;
    }

    public void modifyMember(Long id, MemberModifyReq modifyMember) {
        Member member = memberRepository.findOne(id);
        member.setNickname(modifyMember.getNickname());
        member.setProfile(modifyMember.getProfile());
        memberRepository.save(member);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findOne(id);
    }
}
