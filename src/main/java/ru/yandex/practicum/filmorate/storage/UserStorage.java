package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    User createUser(User user);

    Collection<User> findAll();

    User updateUser(User user);

    User getById(int id);

    User deleteById(int id);

    Map<Integer, User> getUsersMap();

    boolean isAdded(int id);
}
