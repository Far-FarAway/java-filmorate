package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.annotation.OnCreate;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.friends.FriendListStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated
public class UserController {
    UserStorage userStorage;
    FriendListStorage friendListStorage;
    UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @GetMapping("/{userId}/friends")
    public Map<String, List<String>> getFriends(@PathVariable int userId) {
        return userService.getFriends(userId, friendListStorage);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<String> getCommonFriends(@PathVariable int userId, @PathVariable int otherId) {
        return userService.getCommonFriends(userId, otherId, friendListStorage);
    }

    @PostMapping
    public User postUser(@Validated({OnCreate.class, Default.class}) @RequestBody User user) {
        return userStorage.postUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public int addFriend(@PathVariable int userId, @PathVariable int friendId) {
        return userService.addFriend(userId, friendId, friendListStorage);
    }

    @PutMapping
    public boolean updateUser(@RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public boolean deleteUser(@PathVariable int userId) {
        return userStorage.deleteUser(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public boolean deleteUser(@PathVariable int userId, @PathVariable int friendId) {
        return userService.deleteFriend(userId, friendId, friendListStorage);
    }
}
