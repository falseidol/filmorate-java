package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTest {

    static LocalDate time;
    static User user;
    static User user2;
    static UserController userController;

    @BeforeEach
    void init() {
        userController = new UserController();
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

    @AfterEach
    void cleaner() {
        userController.getUsers().clear();
    }

    @Test
    void shouldAddUserToMap() {
        userController.createUser(user);
        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void shouldUpdateUserInMap() {
        userController.createUser(user);
        userController.updateUser(user2);
        Assertions.assertEquals("test2@", user2.getEmail());
        Assertions.assertEquals("test2", user2.getLogin());
        Assertions.assertEquals("test2", user2.getName());
    }
}
