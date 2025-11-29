package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder(toBuilder = true)
public record User (
        //TODO: Implement user domain object
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String loginName,
        String emailAddress,
        String firstName,
        String lastName
) implements Serializable { // serializable to allow cloning (see TestFixtures class).
    @Serial
    private static final long serialVersionUID = 1L;

    public User {
        //ab hier mit CoPilot, simple Fehler via autokorrektur behoben
        // Statische Null-Checks
        Objects.requireNonNull(loginName, "loginName must not be null");
        Objects.requireNonNull(emailAddress, "emailAddress must not be null");
        Objects.requireNonNull(firstName, "firstName must not be null");
        Objects.requireNonNull(lastName, "lastName must not be null");

        // automatische Timestamps: createdAt -> now wenn null, updatedAt -> createdAt wenn null
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }
}
