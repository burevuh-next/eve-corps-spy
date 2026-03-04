package com.example.backend.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backend.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;

	@Override
	protected  void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain chain)throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		try {
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			if (jwtService.validateToken(token)) {
				Long userId = jwtService.extractUserId(token);
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					userId.toString(), null, Collections.emptyList());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		} catch (Exception e) {
			System.err.println("JWT validation error: " + e.getMessage());
		}

		chain.doFilter(request, response);

	}
}
