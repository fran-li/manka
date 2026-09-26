package pe.edu.utec.manka.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pe.edu.utec.manka.dto.UserRegisterRequestDto;
import pe.edu.utec.manka.dto.UserRegisterResponseDto;
import pe.edu.utec.manka.dto.UserResponseDto;
import pe.edu.utec.manka.dto.UserUpdateRequestDto;
import pe.edu.utec.manka.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@Valid @RequestBody UserRegisterRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @GetMapping("/me")
    public UserResponseDto me(@AuthenticationPrincipal Jwt jwt) {
        return userService.getProfile(jwt.getSubject());
    }

    @PatchMapping("/me")
    public UserResponseDto update(@AuthenticationPrincipal Jwt jwt,
                                  @Valid @RequestBody UserUpdateRequestDto request) {
        return userService.updateProfile(jwt.getSubject(), request);
    }
}
