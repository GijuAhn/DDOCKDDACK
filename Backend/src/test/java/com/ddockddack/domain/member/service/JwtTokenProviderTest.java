package com.ddockddack.domain.member.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class JwtTokenProviderTest{
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void createTokenTest(){
        String token = jwtTokenProvider.createToken("somebody");
        System.out.println(">>>>>>>>>> token = " + token);
    }

    @Test
    public void validateTokenTest(){
        String token = jwtTokenProvider.createToken("somebody");
        System.out.println(token);
        jwtTokenProvider.validateToken(token);
    }

}