package com.dev.comingsoon.service;

import com.dev.comingsoon.dto.TokenType;
import com.dev.comingsoon.repository.IRefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {

    private final IRefreshTokenRepository iRefreshTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!authHeader.startsWith(TokenType.Bearer.name())){
            return;
        }

        final String refreshToken = authHeader.substring(7);

        String storedRefreshToken = String.valueOf(iRefreshTokenRepository.findByRefreshToken(refreshToken)
                .map(token->{
                    token.setRevoked(true);
                    iRefreshTokenRepository.save(token);
                    return token;
                })
                .orElse(null));
    }
}
