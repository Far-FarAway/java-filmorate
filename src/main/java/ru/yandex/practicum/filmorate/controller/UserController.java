package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;

import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User postUser(@RequestBody User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ConditionNotMetException("Логин не должен быть пустым");
        } else if (user.getLogin().contains(" ")) {
            throw new ConditionNotMetException("Логин не должен содержать пробелы");
        } else if (ControllerUtility.isDuplicate(users, user.getLogin())) {
            throw new ConditionNotMetException("Логин уже используется");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionNotMetException("Имейл не должен быть пустым");
        } else if (!user.getEmail().contains("@")) {
            throw new ConditionNotMetException("Имейл должен содержать символ \"@\"");
        } else if (ControllerUtility.isDuplicate(users, user.getEmail())) {
            throw new ConditionNotMetException("Имейл уже используется");
        }

        if (user.getBirthday() == null) {
            throw new ConditionNotMetException("Дата рождения не должна быть пустой");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ConditionNotMetException("Дата рождения не должна быть в будущем");
        }

        user.setId(ControllerUtility.getNextId(users.keySet()));
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден");
        }

        User oldUser = users.get(user.getId());

        if (user.getLogin() != null && !user.getLogin().isBlank()) {
            if (user.getLogin().contains(" ")) {
                throw new ConditionNotMetException("Логин не должен содержать пробелы");
            } else if (ControllerUtility.isDuplicate(users, user.getLogin())) {
                throw new ConditionNotMetException("Логин уже используется");
            } else {
                oldUser.setLogin(user.getLogin());
            }
        }

        if (user.getName() == null || user.getName().isBlank()) {
            oldUser.setName(user.getLogin());
        } else {
            oldUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
             if (!user.getEmail().contains("@")) {
                throw new ConditionNotMetException("Имейл должен содержать символ \"@\"");
            } else if (ControllerUtility.isDuplicate(users, user.getEmail())) {
                throw new ConditionNotMetException("Имейл уже используется");
            } else {
                 oldUser.setEmail(user.getEmail());
             }
        }

        if (user.getBirthday() != null) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ConditionNotMetException("Дата рождения не должна быть в будущем");
            } else {
                oldUser.setBirthday(user.getBirthday());
            }
        }

        return oldUser;
    }
}
