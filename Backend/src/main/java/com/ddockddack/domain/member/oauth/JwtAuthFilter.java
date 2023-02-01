package com.ddockddack.domain.member.oauth;

import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.repository.MemberRepository;
import com.ddockddack.domain.member.response.MemberAccessRes;
import com.ddockddack.domain.member.service.TokenService;
import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.AccessDeniedException;
import com.ddockddack.global.error.exception.NotFoundException;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain)
        throws IOException, ServletException {

        log.info("Filter 진입");
        String accessToken = (request).getHeader("access-token");
        String refreshToken = (request).getHeader("refresh-token");

        log.info("accessToken {} ", accessToken);
        log.info("refreshToken {} ", refreshToken);

        if (accessToken != null && tokenService.verifyToken(accessToken)) {
            Long id = tokenService.getUid(accessToken);
            Member member = memberRepository.getReferenceById(id);
            log.info("member {}", member);
            if (member == null) {
                throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
            }

            MemberAccessRes memberAccessRes = new MemberAccessRes(accessToken, member.getId());

            Authentication auth = getAuthentication(memberAccessRes);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else if (refreshToken != null && tokenService.verifyToken(refreshToken)) { //access-token으로 먼저 접근한 경우 클라이언트에 refresh 토큰 보내달라고 알림
            //refresh-token을 받음 access-token 재발급
            Long id = tokenService.getUid(refreshToken);
//            Token token = tokenService.generateToken(id, "USER");
            Member member = memberRepository.getReferenceById(id);

            if (member == null) {
                throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
            }

            MemberAccessRes memberAccessRes = new MemberAccessRes(refreshToken, member.getId());

            Authentication auth = getAuthentication(memberAccessRes);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else { //access token이 만료 된 경우
            throw new AccessDeniedException(ErrorCode.EXPIRED_ACCESSTOKEN);
        }
        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(MemberAccessRes member) {
        return new UsernamePasswordAuthenticationToken(member, "",
            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}