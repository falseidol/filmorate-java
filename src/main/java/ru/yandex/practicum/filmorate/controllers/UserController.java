package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Получен /GET запрос о выводе пользователей");
        return userStorage.findAll();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен /POST запрос создание пользователя");
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен /PUT запрос обновление пользователя");
        return userStorage.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public User removeUserById(@PathVariable int id) {
        return removeUserById(id);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userStorage.getById(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> removeFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.removeFriend(id, friendId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<User> addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        return userService.getUsersFriendListById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getSharedFriendsList(id, otherId);
    }
}

