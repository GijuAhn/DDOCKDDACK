package com.ddockddack.domain.member.config;

import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.entity.Role;
import com.ddockddack.domain.member.repository.MemberRepository;
import com.ddockddack.domain.member.response.MemberLoginPostRes;
import com.ddockddack.domain.member.service.Token;
import com.ddockddack.domain.member.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        var attributes = oAuth2User.getAttributes();

        Member member = memberRepository.getByEmail((String) attributes.get("email"));

        System.out.println((String) attributes.get("email"));
        System.out.println(member);
        log.info("email {} ", (String) attributes.get("email"));
        log.info("email {} ", (String) attributes.get("nickname"));
        if (member == null) {
            member = Member.builder()
                .email((String) attributes.get("email"))
                .nickname((String) attributes.get("nickname"))
                .profile("")
                .role(Role.MEMBER)
                .build();
            memberRepository.save(member);
        }

        Token token = tokenService.generateToken(member.getId().toString(), "USER");
        log.info("JwT : {}", token);
        writeTokenResponse(response, token, member.getId());
    }

    private void writeTokenResponse(HttpServletResponse response, Token token, Long id)
        throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        Member member = memberRepository.getReferenceById(id);
        MemberLoginPostRes memberLoginPostRes = new MemberLoginPostRes(token.getToken(),
            token.getRefreshToken(), member.getId());

        log.info(" {} ", memberLoginPostRes);

        String json = new ObjectMapper().writeValueAsString(memberLoginPostRes);

        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(json);
        response.flushBuffer();
    }
}