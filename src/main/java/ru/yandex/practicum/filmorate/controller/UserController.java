package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.annotation.OnCreate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int userId, @PathVariable int otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

    @PostMapping
    public User postUser(@Validated({OnCreate.class, Default.class}) @RequestBody User user) {
        return userStorage.postUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public boolean addFriend(@PathVariable int userId, @PathVariable int friendId) {
        return userService.addFriend(userId, friendId);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public boolean deleteUser(@PathVariable int userId) {
        return userService.deleteUser(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public boolean deleteUser(@PathVariable int userId, @PathVariable int friendId) {
        return userService.deleteFriend(userId, friendId);
    }
}
