package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();

    private int Id = 0;

    private int makeID() {
        return ++Id;
    }

    @Override
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

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User updateUser(User user) {
        log.info("Проверка наличия в списке");
        validateExistenceForPUT(user);
        validateName(user);
        User userFromCreator = userCreator(user);
        users.put(userFromCreator.getId(), userFromCreator);
        log.info("Пользователь обновлён");
        return user;
    }

    @Override
    public User getById(int id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователя с таким ид не существует.");
        }
        log.info("Выводим пользователя с ид :" + id);
        return users.get(id);
    }

    @Override
    public User deleteById(int id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователя с таким ид не существует.");
        }
        User user = users.get(id);
        log.info("Пользователь с ид :" + id + " удалён.");
        users.remove(id);
        return user;
    }

    @Override
    public Map<Integer, User> getUsersMap() {
        return users;
    }

    @Override
    public boolean isAdded(int id) {
        return users.containsKey(id);
    }

    public User userCreator(User user) {
        log.info("Создаем объект");
        User userFromBuilder = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .friends(new HashSet<>())
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

    private void validateExistenceForPUT(User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Id пользователя '{}' ", user.getId());
            throw new ObjectNotFoundException("Пользователь отсутствует!");
        }
    }
}
