package ru.yandex.practicum.filmorate.storage.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    User createUser(User user);

    Collection<User> findAll();

    User updateUser(User user);

    User getById(int id);

    User deleteById(int id);

    Map<Integer, User> getUsersMap();

    List<Integer> addFriendship(int firstId, int secondId);

    List<Integer> removeFriendship(int firstId, int secondId);

    List<User> getFriendsListById(int id);

    List<User> getSharedFriendsList(int firstId, int secondId);
}
