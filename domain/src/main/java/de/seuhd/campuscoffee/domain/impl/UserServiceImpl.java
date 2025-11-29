package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.ports.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    // TODO: Implement user service
    private final ConcurrentMap<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public User create(User domain) {
        Objects.requireNonNull(domain, "domain user must not be null");
        long id = idGenerator.incrementAndGet();
        // Let the record handle createdAt/updatedAt when passed as null
        User toStore = new User(
                id,
                null,
                null,
                domain.loginName(),
                domain.emailAddress(),
                domain.firstName(),
                domain.lastName()
        );
        store.put(id, toStore);
        log.debug("Created user with id {}", id);
        return toStore;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByLoginName(String loginName) {
        if (loginName == null) return Optional.empty();
        return store.values().stream()
                .filter(u -> loginName.equals(u.loginName()))
                .findFirst();
    }

    @Override
    public Optional<User> update(User domain) {
        Objects.requireNonNull(domain, "domain user must not be null");
        Long id = domain.id();
        if (id == null) {
            return Optional.empty();
        }

        User existing = store.get(id);
        if (existing == null) {
            return Optional.empty();
        }

        // Preserve createdAt, set updatedAt to now
        User updated = new User(
                id,
                existing.createdAt(),
                LocalDateTime.now(),
                domain.loginName(),
                domain.emailAddress(),
                domain.firstName(),
                domain.lastName()
        );

        store.put(id, updated);
        log.debug("Updated user with id {}", id);
        return Optional.of(updated);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) return;
        store.remove(id);
        log.debug("Deleted user with id {}", id);
    }
    @Override
    public User upsert(User domain) {
        Objects.requireNonNull(domain, "domain user must not be null");
        Long id = domain.id();

        // If no id provided -> create new
        if (id == null) {
            return create(domain);
        }

        // If exists -> update preserving createdAt
        User existing = store.get(id);
        if (existing != null) {
            User updated = new User(
                    id,
                    existing.createdAt(),
                    LocalDateTime.now(),
                    domain.loginName(),
                    domain.emailAddress(),
                    domain.firstName(),
                    domain.lastName()
            );
            store.put(id, updated);
            log.debug("Upsert updated user with id {}", id);
            return updated;
        }

        // If id provided but not present -> create entry with given id
        // Ensure idGenerator does not produce duplicate smaller ids
        idGenerator.updateAndGet(curr -> Math.max(curr, id));
        User toStore = new User(
                id,
                null,
                null,
                domain.loginName(),
                domain.emailAddress(),
                domain.firstName(),
                domain.lastName()
        );
        store.put(id, toStore);
        log.debug("Upsert created user with id {}", id);
        return toStore;
    }
}
