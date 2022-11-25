package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private final HashMap<Integer, User> users = new HashMap<>();

    private int Id = 0;

    private int makeID() {
        return ++Id;
    }

    public Collection<User> getUsers() {
        return users.values();
    }


    public User createUser(User user) {
        log.info("Проверка наличия в списке");
        validateExistenceForPOST(user);
        validateName(user);
        log.info("Присвоение id");
        user.setId(makeID());
        User userFromCreator = userCreator(user);
        users.put(userFromCreator.getId(), userFromCreator);
        log.info("Пользователь с именем " + userFromCreator.getName() + "создан");
        return user;
    }

    public User updateUser(User user) {
        log.info("Проверка наличия в списке");
        validateExistenceForPUT(user);
        validateName(user);
        User userFromCreator = userCreator(user);
        users.put(userFromCreator.getId(), userFromCreator);
        log.info("Пользователь обновлён");
        return user;
    }

    private User userCreator(User user) {
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

    private void validateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Присваиваем поле login '{}' для поля name '{}' ", user.getLogin(), user.getName());
            user.setName(user.getLogin());
        }
    }

    private void validateExistenceForPOST(User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            log.info("Id пользователя '{}' ", user.getId());
            throw new ValidationException("Пользователь с таким id уже существует!");
        }
    }

    private void validateExistenceForPUT(User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.info("Id пользователя '{}' ", user.getId());
            throw new ValidationException("Пользователь отсутствует!");
        }
    }
}
