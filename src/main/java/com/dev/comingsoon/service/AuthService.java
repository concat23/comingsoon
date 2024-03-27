package com.dev.comingsoon.service;

import com.dev.comingsoon.config.jwtauth.JwtTokenGenerator;
import com.dev.comingsoon.dto.AdminUserLoginDTO;
import com.dev.comingsoon.dto.AdminUserRegistrationDTO;
import com.dev.comingsoon.dto.AuthResponseDTO;
import com.dev.comingsoon.dto.TokenType;
import com.dev.comingsoon.entity.AdminUser;
import com.dev.comingsoon.entity.RefreshToken;
import com.dev.comingsoon.mapper.AdminUserMapper;
import com.dev.comingsoon.repository.IAdminUserRepository;
import com.dev.comingsoon.repository.IRefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final IAdminUserRepository iAdminUserRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final IRefreshTokenRepository iRefreshTokenRepository;
    private final AdminUserMapper adminUserMapper;
    private final AuthenticationManager authenticationManager;



    public AuthResponseDTO getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try
        {
             AdminUser adminUser = iAdminUserRepository.findByEmail(authentication.getName())
                    .orElseThrow(()->{
                        log.error("[AuthService:userSignInAuth] User :{} not found",authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
            //Let's save the refreshToken as well
            saveUserRefreshToken(adminUser,refreshToken);
            //Creating the cookie
            creatRefreshTokenCookie(response,refreshToken);
            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",adminUser.getUserName());
            return  AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(adminUser.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }

    private void saveUserRefreshToken(AdminUser adminUser, String refreshToken) {
        var refreshTokenEntity = RefreshToken.builder()
                .adminUser(adminUser)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        iRefreshTokenRepository.save(refreshTokenEntity);
    }

    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {

        if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
        RefreshToken refreshTokenEntity = iRefreshTokenRepository.findByRefreshToken(refreshToken)
                .filter(tokens-> !tokens.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

        AdminUser adminUserEntity = refreshTokenEntity.getAdminUser();

        //Now create the Authentication object
        Authentication authentication =  createAuthenticationObject(adminUserEntity);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return  AuthResponseDTO.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(adminUserEntity.getUserName())
                .tokenType(TokenType.Bearer)
                .build();
    }

    private static Authentication createAuthenticationObject(AdminUser adminUser) {
        // Extract user details from UserDetailsEntity
        String username = adminUser.getEmail();
        String password = adminUser.getPassword();
        String roles = adminUser.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    public AuthResponseDTO registerUser(AdminUserRegistrationDTO adminUserRegistrationDTO,
                                        HttpServletResponse httpServletResponse) {
        try{
            log.info("[AuthService:registerUser]User Registration Started with :::{}",adminUserRegistrationDTO);

            Optional<AdminUser> adminUser = iAdminUserRepository.findByEmail(adminUserRegistrationDTO.getUserEmail());
            if(adminUser.isPresent()){
                throw new Exception("User Already Exist");
            }

            AdminUser adminUserDetailsEntity = adminUserMapper.convertToEntity(adminUserRegistrationDTO);
            Authentication authentication = createAuthenticationObject(adminUserDetailsEntity);


            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            AdminUser savedAdminUserDetails = iAdminUserRepository.save(adminUserDetailsEntity);
            saveUserRefreshToken(savedAdminUserDetails,refreshToken);

            creatRefreshTokenCookie(httpServletResponse,refreshToken);

            log.info("[AuthService:registerUser] User:{} Successfully registered",savedAdminUserDetails.getUserName());
            return   AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedAdminUserDetails.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
    public Authentication authenticate(AdminUserLoginDTO adminUserLoginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminUserLoginDTO.getUsernameOrEmail(),adminUserLoginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

}
