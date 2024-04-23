package com.pratik.backendproductapp.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pratik.backendproductapp.config.CustomerUsersDetailsService;
import com.pratik.backendproductapp.jwt.JwtService;

import io.jsonwebtoken.Claims;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomerUsersDetailsService customerUsersDetailsService;
    
    Claims claims=null;
    
    private String userName =null;
    
   
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       if(request.getServletPath().matches("/users/login|/users/signup|/users/forgotPassword")) {
    	   filterChain.doFilter(request, response);
       }
       else {
    	   String authorizationHeader  = request.getHeader("Authorization");
    	   String token =null;
    	   if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
               token = authorizationHeader.substring(7);
               userName = jwtService.extractUsername(token);
               claims = jwtService.extractAllClaims(token);
           }
    	   if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               UserDetails userDetails =customerUsersDetailsService .loadUserByUsername(userName);
               if (jwtService.validateToken(token, userDetails)) {
                   UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                   authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authToken);
               }
           }
    	   filterChain.doFilter(request, response);
       }
    }
    
    public boolean isAdmin() {
    	return "admin".equalsIgnoreCase((String) claims.get("role"));
    } 
    
    public boolean isUser() {
    	return "user".equalsIgnoreCase((String) claims.get("role"));
    } 
    
    public String getCurrentUser() {
    	return userName;
    }
}
