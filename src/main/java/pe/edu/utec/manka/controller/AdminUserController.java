package pe.edu.utec.manka.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.utec.manka.dto.UserResponseDto;
import pe.edu.utec.manka.dto.UserRolesUpdateRequestDto;
import pe.edu.utec.manka.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{id}/roles")
    public UserResponseDto updateRoles(
            @PathVariable Long id,
            @Valid @RequestBody UserRolesUpdateRequestDto request) {
        return userService.updateRoles(id, request.getRoles());
    }
}
