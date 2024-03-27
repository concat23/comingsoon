package com.dev.comingsoon.resource;


import com.dev.comingsoon.dto.AdminUserLoginDTO;
import com.dev.comingsoon.dto.AdminUserRegistrationDTO;
import com.dev.comingsoon.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/admin/auth")
@Tag(name = "Auth Controller")
public class AuthController {
    private final AuthService authService;

//    @Operation(summary = "Sign in")
//    @PostMapping("/sign-in")
//    public ResponseEntity<?> authenticateUser(Authentication authentication, HttpServletResponse response){
//
//        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication,response));
//    }
    @Operation(summary = "Sign in")
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody AdminUserLoginDTO adminUserLoginDTO, HttpServletResponse response){

        Authentication authentication = authService.authenticate(adminUserLoginDTO);
        if (authentication != null) {
            // If authentication successful, return JWT tokens
            return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication, response));
        } else {
            // If authentication fails, return unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @Operation(summary = "Refesh token")
    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @Operation(summary = "Sign up")
    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AdminUserRegistrationDTO adminUserRegistrationDTO,
                                          BindingResult bindingResult, HttpServletResponse httpServletResponse){

        log.info("[AuthController:registerUser]Signup Process Started for user:{}",adminUserRegistrationDTO.getUserName());
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:registerUser]Errors in user:{}",errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.ok(authService.registerUser(adminUserRegistrationDTO,httpServletResponse));
    }
}
