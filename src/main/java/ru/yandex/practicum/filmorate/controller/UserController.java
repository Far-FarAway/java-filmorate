package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    InMemoryUserStorage userStorage;
    UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        return userStorage.postUser(user);
    }

    @PostMapping("/{userId}/{friendId}")
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
}
