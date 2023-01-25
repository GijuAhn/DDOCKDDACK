package com.ddockddack.domain.member.service;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenProvider {
	private final String secretKey="ddockddack";
	private long validityInMilliseconds= 24*60*60*1000L; // 1 day

	// 토큰 생성
	public String createToken(String subject) {
		Claims claims = Jwts.claims().setSubject(subject);
		Date now = new Date();	
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	// 토큰 파싱
	public String getSubject(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	//	토큰 유효성 검증
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}