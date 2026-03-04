package com.example.backend.service;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private static final long EXPIRATION_TIME = 864000000;

	public String generateToken(Long userId) {
		return Jwts.builder()
		.setSubject(userId.toString())
		.setIssuedAt(new Date())
		.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
		.signWith(SECRET_KEY)
		.compact();
	}
	public Long extractUserId(String token) {
		return Long.parseLong(Jwts.parserBuilder()
		.setSigningKey(SECRET_KEY)
		.build()
		.parseClaimsJws(token)
		.getBody()
		.getSubject());
	}

	public String extractEmail(String token) {
		return Jwts.parserBuilder()
		.setSigningKey(SECRET_KEY)
		.build()
		.parseClaimsJws(token)
		.getBody()
		.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
