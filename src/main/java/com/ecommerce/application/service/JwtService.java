package com.ecommerce.application.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.application.dao.UserDao;
import com.ecommerce.application.entity.JwtRequest;
import com.ecommerce.application.entity.JwtResponse;
import com.ecommerce.application.entity.Users;
import com.ecommerce.application.util.JwtUtil;


@Service
public class JwtService implements UserDetailsService{
	
	@Autowired
	private UserDao userdao;
	
	@Autowired
	private JwtUtil jwtutil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
		String userName = jwtRequest.getUserName();
		String userPassword = jwtRequest.getPassword();
		
		authenticate(userName,userPassword);
		final UserDetails userDetails = loadUserByUsername(userName);
		String newGeneratedToken = jwtutil.generateToken(userDetails);
		Users user = userdao.findById(userName).get();
		 return new JwtResponse(user,newGeneratedToken);
		
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Users user = userdao.findById(username).get();
		if(user != null) {
			return new User(user.getUserName(),user.getUserPassword(),getAuthoriites(user));
		}else {
			throw new UsernameNotFoundException("Username is not valid");
		}
	}
	
	private Set getAuthoriites(Users user){
		Set authorities = new HashSet();
		user.getRole().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
		});
		
		return authorities;
	}
	
	private void authenticate(String username , String userPassword) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,userPassword));
			
		}catch(DisabledException e) {
			throw new Exception("User is disabled");
		}catch(BadCredentialsException e) {
			throw new Exception("Bad creds from user");
		}
		}

}
