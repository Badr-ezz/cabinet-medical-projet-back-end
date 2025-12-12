package com.example.user_service.config;

import com.example.user_service.service.jwtServices.JwtUtils;
import com.example.user_service.service.jwtServices.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService; // injection du service blacklist

    @Autowired
    public JwtAuthFilter(JwtUtils jwtUtils,
                         UserDetailsService userDetailsService,
                         TokenBlacklistService tokenBlacklistService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

//            if (isProtectedEndpoint(request) && jwt == null) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: No token provided");
//                return;
//            }

            if (jwt != null) {
                if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalidé (blacklist)");
                    return;
                }
                if (!jwtUtils.validateJwtToken(jwt)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                    return;
                }

                String username = jwtUtils.extractUsername(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                String roles = jwtUtils.extractClaim(jwt, claims -> claims.get("roles", String.class));
                List<GrantedAuthority> authorities = Arrays.stream(roles.split(","))
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }


}
