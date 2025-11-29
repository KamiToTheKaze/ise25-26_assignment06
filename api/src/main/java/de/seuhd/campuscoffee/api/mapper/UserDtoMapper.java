package de.seuhd.campuscoffee.api.mapper;
//Imports für generierten Code nachgetragen
import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.domain.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

public interface UserDtoMapper {
    //TODO: Implement user DTO mapper
    //generierter Code von CoPilot, einfache Fehler erneut via Autokorrektur behoben
    static User toDomain(UserDto dto) {
        Objects.requireNonNull(dto, "UserDto must not be null");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime created = dto.createdAt() == null ? now : dto.createdAt();
        LocalDateTime updated = dto.updatedAt() == null ? created : dto.updatedAt();

        return new User(
                dto.id(),
                created,
                updated,
                Objects.requireNonNull(dto.loginName(), "loginName must not be null"),
                Objects.requireNonNull(dto.emailAddress(), "emailAddress must not be null"),
                Objects.requireNonNull(dto.firstName(), "firstName must not be null"),
                Objects.requireNonNull(dto.lastName(), "lastName must not be null")
        );
    }

    static UserDto toDto(User user) {
        Objects.requireNonNull(user, "User must not be null");
        return new UserDto(
                user.id(),
                user.createdAt(),
                user.updatedAt(),
                user.loginName(),
                user.emailAddress(),
                user.firstName(),
                user.lastName()
        );
    }

    static UserDto fromDomain(User userToCreate) {
        Objects.requireNonNull(userToCreate, "User must not be null");
        return new UserDto(
                null, // id für Create/Upsert-Anfragen leer lassen
                null, // createdAt vom Server setzen lassen
                null, // updatedAt vom Server setzen lassen
                userToCreate.loginName(),
                userToCreate.emailAddress(),
                userToCreate.firstName(),
                userToCreate.lastName()
        );
    }
}