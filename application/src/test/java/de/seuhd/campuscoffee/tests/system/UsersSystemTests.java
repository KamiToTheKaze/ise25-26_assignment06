package de.seuhd.campuscoffee.tests.system;
//Imports für generierten Code nachgetragen

import de.seuhd.campuscoffee.api.mapper.UserDtoMapper;
import de.seuhd.campuscoffee.domain.impl.UserServiceImpl;
import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static de.seuhd.campuscoffee.tests.SystemTestUtils.Requests.userRequests;
import static org.junit.jupiter.api.Assertions.*;

public class UsersSystemTests extends AbstractSysTest {

    //TODO: Uncomment once user endpoint is implemented

    @Test
    void createUser() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        User createdUser = UserDtoMapper.toDomain(userRequests.create(List.of(UserDtoMapper.fromDomain(userToCreate))).getFirst());

        assertEqualsIgnoringIdAndTimestamps(createdUser, userToCreate);
    }

    //TODO: Add at least two additional tests for user operations
    //Mehr oder weniger mit Copilot generiert, SytemTestUtils konnte nicht richtig gelesen werden, von Hand nachgearbeitet
    @Test
    void getUserById() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        User createdUser = UserDtoMapper.toDomain(userRequests.create(List.of(UserDtoMapper.toDto(userToCreate))).getFirst());

        // Abruf per ID
        User fetched = UserDtoMapper.toDomain(userRequests.retrieveById(createdUser.id()));
        assertEqualsIgnoringIdAndTimestamps(fetched, userToCreate);
    }
    @Test
    void createUser_setsIdAndStores() {
        UserServiceImpl service = new UserServiceImpl();

        User toCreate = new User(
                null, // id
                null, // createdAt
                null, // updatedAt
                "simple_user",
                "simple@example.com",
                "Simple",
                "User"
        );

        User created = service.create(toCreate);

        assertNotNull(created);
        assertNotNull(created.id(), "Erwartet eine gesetzte ID nach create()");
        assertEquals("simple_user", created.loginName());
    }
}