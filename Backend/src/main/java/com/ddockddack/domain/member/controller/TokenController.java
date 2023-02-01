package com.ddockddack.domain.member.controller;

import com.ddockddack.domain.member.entity.Member;
import com.ddockddack.domain.member.oauth.Token;
import com.ddockddack.domain.member.response.MemberLoginPostRes;
import com.ddockddack.domain.member.service.TokenService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ddockddack.global.error.ErrorCode;
import com.ddockddack.global.error.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class TokenController {

    private final TokenService tokenService;
    private final RedisTemplate redisTemplate;

    //  Access token expire endpoint
    @GetMapping("/token/expired")
    public String auth() {
        throw new RuntimeException();
    }

    @Operation(summary = "Access 토큰 재발급", description = "Access 토큰 재발급 메소드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/token/refresh")
    public ResponseEntity refreshAuth(@RequestHeader(value = "refresh-token", required = false) String refreshToken) {
        log.info("refresh 진입");

        if (refreshToken != null && tokenService.verifyToken(refreshToken)) {
            if(redisTemplate.opsForValue().get(refreshToken) == null) {
                throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
            }

            Long id = tokenService.getUid(refreshToken);
            Token token = tokenService.generateToken(id, "USER");

            MemberLoginPostRes memberLoginPostRes = new MemberLoginPostRes(token.getToken(),
                    token.getRefreshToken(), id);

            return ResponseEntity.ok(memberLoginPostRes);
        }

        throw new RuntimeException();
    }
}
