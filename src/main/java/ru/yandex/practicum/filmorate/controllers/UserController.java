package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    HashMap<Integer, User> users = new HashMap<>();

    private int Id = 0;

    private int makeID() {
        return ++Id;
    }

    @GetMapping("/users")
    public HashMap<Integer, User> getUsers() {
        log.info("Получен /GET запрос о выводе пользователей");
        return users;
    }

    @PostMapping(value = "/user/create")
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен /POST запрос создание пользователя");
        log.info("Проверка наличия в списке");
        validateExistenceForPOST(user);
        validateName(user);
        validateLogin(user);
        log.info("Присвоение id");
        user.setId(makeID());
        User userFromCreator = userCreator(user);
        users.put(userFromCreator.getId(), userFromCreator);
        log.info("Пользователь с именем " + userFromCreator.getName() + "создан");
        return user;
    }

    @PutMapping(value = "/user/update")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен /POST запрос обновление пользователя");
        try {
            log.info("Проверка наличия в списке");
            validateExistenceForPUT(user);
            validateName(user);
            validateLogin(user);
            User userFromCreator = userCreator(user);
            users.put(userFromCreator.getId(), userFromCreator);
            log.info("Пользователь обновлён");
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User userCreator(User user) {
        log.info("Создаем объект");
        User userFromBuilder = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
        log.info("Объект User создан, имя : '{}'", userFromBuilder.getName());
        return userFromBuilder;
    }

    private User validateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Присваиваем поле login '{}' для поля name '{}' ", user.getLogin(), user.getName());
            user.setName(user.getLogin());
        }
        return user;
    }

    private User validateLogin(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.info("Пробел в поле login '{}' ", user.getLogin());
            throw new ValidationException("В поле логин не должно быть пробелов");
        }
        return user;
    }

    public void validateExistenceForPOST(User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            log.info("Id пользователя '{}' ", user.getId());
            throw new ValidationException("Пользователь с таким id уже существует!");
        }
    }

    public void validateExistenceForPUT(User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.info("Id пользователя '{}' ", user.getId());
            throw new ValidationException("Пользователь отсутствует!");
        }
    }
}

