package com.ecommerce.application.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.application.service.JwtService;
import com.ecommerce.application.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired 
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String header = request.getHeader("Authorization");
		String jwtToken = null;
		String userName = null;
		System.out.println("checking for incoming headers" + header);
		if(header != null && header.startsWith("Bearer ")) {
			jwtToken  = header.substring(7);
			
			try {
				userName = jwtUtil.getUserNameFromToken(jwtToken);
			}catch(IllegalArgumentException e){
				System.out.println("Unable to get Jwt token");
			}catch(ExpiredJwtException s) {
				System.out.println("Jwt token is expired");
			}
		}else {
			System.out.println("Jwt token does not start with bearer");
		}
		
		if(userName !=null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = jwtService.loadUserByUsername(userName);
			
			if(jwtUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamepasswordAuthenticationtoken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				
				usernamepasswordAuthenticationtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamepasswordAuthenticationtoken);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
