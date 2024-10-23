package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    UserStorage userStorage;
    UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    @GetMapping("/{userId}/friends")
    public Collection<Integer> getFriends(@PathVariable int userId) {
        return userService.getFriends(userId, userStorage);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<Integer> getCommonFriends(@PathVariable int userId, @PathVariable int otherId) {
        return userService.getCommonFriends(userId, otherId, userStorage);
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        return userStorage.postUser(user);
    }

    @PostMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable int userId, @PathVariable int friendId) {
        return userService.addFriend(userId, friendId, userStorage);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public User deleteUser(@PathVariable int userId) {
        return userStorage.deleteUser(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public Collection<Integer> deleteUser(@PathVariable int userId, @PathVariable int friendId) {
        return userService.deleteUser(userId, friendId, userStorage);
    }
}
