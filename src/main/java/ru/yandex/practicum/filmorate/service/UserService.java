package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    public List<Integer> addFriend(int firstId, int secondId) {
        return userStorage.addFriendship(firstId, secondId);
    }

    public List<Integer> removeFriend(int firstId, int secondId) {
        return userStorage.removeFriendship(firstId, secondId);
    }

    public List<User> getUsersFriendListById(int firstId) {
        return userStorage.getFriendsListById(firstId);
    }

    public List<User> getSharedFriendsList(int firstId, int secondId) {
        return userStorage.getSharedFriendsList(firstId, secondId);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validate(user);
        return userStorage.createUser(user);
    }

    public User update(User user) {
        validate(user);
        return userStorage.updateUser(user);
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }

    public User getById(int id) {
        return userStorage.getById(id);
    }

    public User deleteById(int id) {
        return userStorage.deleteById(id);

    }
}
