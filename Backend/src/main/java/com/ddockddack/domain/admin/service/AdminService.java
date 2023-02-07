package com.ddockddack.domain.admin.service;

import com.ddockddack.domain.member.service.MemberService;
import com.ddockddack.domain.member.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final TokenService tokenService;
    private final MemberService memberService;
    public boolean isAdminByAccessToken(String accessToken){
        return memberService.isAdmin(tokenService.getUid(accessToken));
    }
}
