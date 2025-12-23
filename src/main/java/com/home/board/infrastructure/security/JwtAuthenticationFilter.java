package com.home.board.infrastructure.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.home.board.application.service.UserService;
import com.home.board.common.util.CookieUtils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request).orElse(null);
            if (token != null) {
                Long userId = tokenProvider.getUserIdFromJWT(token);
                if (tokenProvider.validateToken(token)) {
                    setAuthenticationToContext(userId, request);
                }
            }
        } catch (ExpiredJwtException ex) {
            handleExpiredToken(request, response, ex);
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException ex) {
        try {
            Long userId = Long.parseLong(ex.getClaims().getSubject());
            Optional<String> refreshToken = userService.getRefreshTokenForUser(userId);

            if (refreshToken.isPresent() && tokenProvider.validateToken(refreshToken.get())) {
                tokenProvider.createAndSetTokens(userId, response);
                setAuthenticationToContext(userId, request);

                log.debug("Access token refreshed successfully for user: {}", userId);
            } else {
                log.debug("Invalid refresh token. User needs to re-authenticate.");
            }
        } catch (Exception e) {
            log.error("Error while handling expired token", e);
        }
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7));
        }
        return CookieUtils.getCookie(request, "access_token")
                .map(Cookie::getValue);
    }

    private void setAuthenticationToContext(Long userId, HttpServletRequest request) {
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}