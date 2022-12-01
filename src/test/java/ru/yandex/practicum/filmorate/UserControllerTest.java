package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

    private static LocalDate time;
    private static User user;
    private static User user2;
    private static UserStorage userStorage;

    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        time = LocalDate.of(2002, 8, 2);
        user = User.builder()
                .id(1)
                .name("test")
                .email("test@")
                .login("test")
                .birthday(time)
                .build();
        user2 = User.builder()
                .id(1)
                .name("test2")
                .email("test2@")
                .login("test2")
                .birthday(time)
                .build();
    }


    @Test
    void shouldAddUserToMap() {
        userStorage.createUser(user);
        Assertions.assertEquals(1, userStorage.getUsersMap().size());
    }

    @Test
    void shouldCreateUserWithIdEquals1() {
        User userFormTest = userStorage.createUser(user);
        assertEquals(1, userFormTest.getId());
    }

    @Test
    void shouldCreateUserWithEmptyName() {
        userStorage.createUser(user);
        assertEquals("test", user.getName());
    }

    @Test
    void shouldFailValidationId() {
        userStorage.createUser(user);
        assertThrows(ValidationException.class, () -> userStorage.createUser(user));
    }

    @Test
    void shouldUpdateUserInMap() {
        userStorage.createUser(user);
        userStorage.updateUser(user2);
        Assertions.assertEquals("test2@", user2.getEmail());
        Assertions.assertEquals("test2", user2.getLogin());
        Assertions.assertEquals("test2", user2.getName());
    }

    @Test
    void validateNameTest() {
        user = User.builder()
                .id(1)
                .name("")
                .email("test@")
                .login("testlogin")
                .birthday(time)
                .build();
        userStorage.createUser(user);
        assertEquals("testlogin", user.getName());
    }

    @Test
    void validateExistenceForPOSTTest() {
        userStorage.createUser(user);
        assertThrows(ValidationException.class, () -> userStorage.createUser(user));
    }

    @Test
    void validateExistenceForPUTTest() {
        assertThrows(ObjectNotFoundException.class, () -> userStorage.updateUser(user));
    }
}
