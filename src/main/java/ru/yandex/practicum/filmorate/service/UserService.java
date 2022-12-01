package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> addFriend(int firstId, int secondId) {
        if (!userStorage.getUsersMap().containsKey(firstId) || !userStorage.getUsersMap().containsKey(secondId)) {
            throw new ObjectNotFoundException(String.format("Пользователя с id %d или %d не существует", firstId, secondId));
        }
        if (userStorage.getById(firstId).getFriends().contains(secondId)) {
            throw new InternalException("Пользователи уже являются друзьями");
        }
        User user1 = userStorage.getById(firstId);
        User user2 = userStorage.getById(secondId);

        user1.getFriends().add(secondId);
        user2.getFriends().add(firstId);
        log.info("Пользователи " + userStorage.getById(firstId).getName() + " и " + userStorage.getById(secondId).getName() + " теперь друзья.");
        return Arrays.asList(userStorage.getById(firstId), userStorage.getById(secondId));
    }


    public List<User> removeFriend(int firstId, int secondId) {
        if (!userStorage.getUsersMap().containsKey(firstId) || !userStorage.getUsersMap().containsKey(secondId)) {
            throw new ObjectNotFoundException("Пользователя с ид " + firstId + " не существует.");
        }
        if (!userStorage.getById(firstId).getFriends().contains(secondId)) {
            throw new InternalException("Пользователи не являются друзьями.");
        }
        User user1 = userStorage.getById(firstId);
        User user2 = userStorage.getById(secondId);

        user1.getFriends().remove(secondId);
        user2.getFriends().remove(firstId);
        log.info("Пользователи " + userStorage.getById(firstId).getName() + " и " + userStorage.getById(secondId).getName() + " больше не дружочки пирожочки.");
        return Arrays.asList(userStorage.getById(firstId), userStorage.getById(secondId));
    }

    public List<User> getUsersFriendListById(int firstId) {
        if (userStorage.getUsersMap().isEmpty()) {
            throw new InternalException("У юзера нету дружочков пирожочков.");
        } else if (!userStorage.getUsersMap().containsKey(firstId)) {
            throw new ObjectNotFoundException("Пользователя с таким ид не существует." + firstId);
        }
        log.info("Выводим список друзей юзера.");
        return userStorage.getById(firstId).getFriends().stream().map(userStorage::getById).collect(Collectors.toList());
    }

    public List<User> getSharedFriendsList(int firstId, int secondId) {
        if (!userStorage.getUsersMap().containsKey(firstId) || !userStorage.getUsersMap().containsKey(secondId)) {
            throw new ObjectNotFoundException("Пользователи не найдены");
        }
        User user = userStorage.getById(firstId);
        User otherUser = userStorage.getById(secondId);
        log.info("Список общих друзей {} и {} отправлен", user.getName(), otherUser.getName());

        return user.getFriends().stream()
                .filter(friendId -> otherUser.getFriends().contains(friendId))
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }
}
