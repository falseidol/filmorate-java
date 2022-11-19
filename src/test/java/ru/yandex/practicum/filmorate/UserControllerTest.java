package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

   private static LocalDate time;
    private static User user;
    private static User user2;
    private static UserService userService;

    @BeforeEach
    void init() {
        userService = new UserService();
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
        userService.createUser(user);
        Assertions.assertEquals(1, userService.getUsers().size());
    }

    @Test
    void shouldCreateUserWithIdEquals1() {
        User userFormTest = userService.createUser(user);
        assertEquals(1, userFormTest.getId());
    }

    @Test
    void shouldCreateUserWithEmptyName() {
        userService.createUser(user);
        assertEquals("test", user.getName());
    }

    @Test
    void shouldFailValidationId() {
        userService.createUser(user);
        assertThrows(ValidationException.class, () -> userService.createUser(user));
    }

    @Test
    void shouldUpdateUserInMap() {
        userService.createUser(user);
        userService.updateUser(user2);
        Assertions.assertEquals("test2@", user2.getEmail());
        Assertions.assertEquals("test2", user2.getLogin());
        Assertions.assertEquals("test2", user2.getName());
    }
}
