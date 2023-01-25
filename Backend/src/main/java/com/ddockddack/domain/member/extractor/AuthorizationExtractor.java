package com.ddockddack.domain.member.extractor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor {
	public static final String AUTHORIZATION = "Authorization";
	public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";

	public String extract(HttpServletRequest request, String type) {
		Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
		while (headers.hasMoreElements()) {
			String value = headers.nextElement();
			if (value.toLowerCase().startsWith(type.toLowerCase())) {
				return value.substring(type.length()).trim();
			}
		}
		return Strings.EMPTY;
	}
}
