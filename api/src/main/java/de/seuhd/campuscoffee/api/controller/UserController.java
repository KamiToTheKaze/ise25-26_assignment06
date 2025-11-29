package de.seuhd.campuscoffee.api.controller;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.api.mapper.UserDtoMapper;
import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.ports.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Users", description = "Operations related to user management.")
@Controller
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    //TODO: Implement user controller
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.debug("GET /api/users - retrieve all users");
        return userService.findAll().stream()
                .map(UserDtoMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.debug("GET /api/users/{} - retrieve user by id", id);
        return userService.findById(id)
                .map(UserDtoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public ResponseEntity<UserDto> getUserByLoginName(@RequestParam("loginName") String loginName) {
        log.debug("GET /api/users/filter?loginName={} - retrieve user by loginName", loginName);
        return userService.findByLoginName(loginName)
                .map(UserDtoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        log.debug("POST /api/users - create user: {}", dto);
        User created = userService.create(UserDtoMapper.toDomain(dto));
        UserDto response = UserDtoMapper.toDto(created);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        log.debug("PUT /api/users/{} - update user: {}", id, dto);
        // Kombiniere Pfad-id mit DTO-Feldern für das Domain-Objekt
        User toUpdate = new User(
                id,
                dto.createdAt(),
                dto.updatedAt(),
                dto.loginName(),
                dto.emailAddress(),
                dto.firstName(),
                dto.lastName()
        );

        return userService.update(toUpdate)
                .map(UserDtoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("DELETE /api/users/{} - delete user", id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
