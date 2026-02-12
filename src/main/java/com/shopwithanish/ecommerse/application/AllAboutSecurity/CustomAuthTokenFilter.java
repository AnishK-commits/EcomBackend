package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JWTutil jwTutil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("JWT FILTER CALLED for URI: {}", request.getRequestURI());
        try {
            String token = jwTutil.getJwtTokenInsideCookie(request);

            log.info("JWT FROM COOKIE: {}", token);


            if (token != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwTutil.validateToken(token)) {




                    String username = jwTutil.getUsernameFromTokenFn(token);
                    log.info("JWT VALID → setting authentication for user: {}", username);
                    UserDetails userDetails =
                            userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("JWT authentication failed", e);
        }

        filterChain.doFilter(request, response);
    }

    // Skip filter for public endpoints
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/")
                || path.startsWith("/api/public/");
    }
}