package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //TODO: Define user service interface
    User create(User domain);

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByLoginName(String loginName);

    Optional<User> update(User domain);

    void deleteById(Long id);
    //hier hab ich in grenzenloser Weisheit Object statt User genommen, was sup-optimal war
    User upsert(User user);
}
