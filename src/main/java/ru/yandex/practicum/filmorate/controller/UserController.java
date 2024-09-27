package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;
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
    private static final Logger log = ((Logger) LoggerFactory.getLogger(UserController.class));

    @GetMapping
    public Collection<User> getUsers() {
        log.setLevel(Level.DEBUG);
        log.info("Получение списка пользовательей");
        return users.values();
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        log.setLevel(Level.DEBUG);
        log.info("Добавление нового пользователя");

        /*
        Я не смог понять, как можно провести валидацию данных в тестах, если эта уже валидация проходит
        на моменте запроса, однако при обычном обращении к методу этого не происходит и как правильно запускать
        спринг приложение в тестах, чтобы делать к нему запросы я пока не знаю
        */
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Логин не введен");
            throw new ConditionNotMetException("Логин не должен быть пустым");
        } else if (user.getLogin().contains(" ")) {
            log.warn("Логин содержит пробелы: {}", user.getLogin());
            throw new ConditionNotMetException("Логин не должен содержать пробелы");
        } else if (ControllerUtility.isDuplicate(users, user.getLogin())) {
            log.warn("Логин {} занят другим пользователем", user.getLogin());
            throw new ConditionNotMetException("Логин уже используется");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя не введено, замена на логин: {}", user.getLogin());
            user.setName(user.getLogin());
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Имейл не введен");
            throw new ConditionNotMetException("Имейл не должен быть пустым");
        } else if (!user.getEmail().contains("@")) {
            log.warn("Имейл не содержит \"@\": {}", user.getEmail());
            throw new ConditionNotMetException("Имейл должен содержать символ \"@\"");
        } else if (ControllerUtility.isDuplicate(users, user.getEmail())) {
            log.warn("Имейл {} занят другим пользователем", user.getEmail());
            throw new ConditionNotMetException("Имейл уже используется");
        }

        if (user.getBirthday() == null) {
            log.warn("Дата рождения не введена");
            throw new ConditionNotMetException("Дата рождения не должна быть пустой");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рожденеия введена в будущем: {}", user.getBirthday());
            throw new ConditionNotMetException("Дата рождения не должна быть в будущем");
        }

        int id = ControllerUtility.getNextId(users.keySet());
        log.debug("Присовение id {} пользователю {}", id, user.getLogin());
        user.setId(id);
        log.debug("Добавление пользователя {} в список пользовотелей(размер списка {})",
                user.getLogin(), users.size());
        users.put(user.getId(), user);

        log.info("Пользователь {} с id {} успешно добавлен", user.getLogin(), user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.setLevel(Level.DEBUG);
        log.info("Обновленние данных пользователя с id {}", user.getId());
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с id {} не найден", user.getId());
            throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден");
        }

        User oldUser = users.get(user.getId());

        if (user.getLogin() != null && !user.getLogin().isBlank() && !oldUser.getLogin().equals(user.getLogin())) {
            if (user.getLogin().contains(" ")) {
                log.warn("Догин содержит пробелы: {}", user.getLogin());
                throw new ConditionNotMetException("Логин не должен содержать пробелы");
            } else if (ControllerUtility.isDuplicate(users, user.getLogin())) {
                log.warn("Логин {} занят другим пользователем", user.getLogin());
                throw new ConditionNotMetException("Логин уже используется");
            } else {
                log.debug("Замена старого логина {} на новый {}", oldUser.getLogin(), user.getLogin());
                oldUser.setLogin(user.getLogin());
            }
        }

        if (!oldUser.getName().equals(user.getName())) {
            if (user.getName() == null || user.getName().isBlank()) {
                log.debug("Имя не введено, замена старого имени {} на логин: {}", oldUser.getName(), user.getLogin());
                oldUser.setName(user.getLogin());
            } else {
                log.debug("Замена страого имени {} на новое {}", oldUser.getName(), user.getName());
                oldUser.setName(user.getName());
            }
        }

        if (user.getEmail() != null && !user.getEmail().isBlank() && !oldUser.getEmail().equals(user.getEmail())) {
            if (!user.getEmail().contains("@")) {
                log.warn("Имейл не содержит \"@\": {}", user.getEmail());
                throw new ConditionNotMetException("Имейл должен содержать символ \"@\"");
            } else if (ControllerUtility.isDuplicate(users, user.getEmail())) {
                log.warn("Имейл {} занят другим пользователем", user.getEmail());
                throw new ConditionNotMetException("Имейл уже используется");
            } else {
                log.debug("Замена старого имейла {} на новый {}", oldUser.getEmail(), user.getEmail());
                oldUser.setEmail(user.getEmail());
            }
        }

        if (user.getBirthday() != null && !oldUser.getBirthday().equals(user.getBirthday())) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.warn("Дата рожденеия введена в будущем: {}", user.getBirthday());
                throw new ConditionNotMetException("Дата рождения не должна быть в будущем");
            } else {
                log.warn("Замена старой даты рождения {} на новую {}",
                        oldUser.getBirthday(), user.getBirthday());
                oldUser.setBirthday(user.getBirthday());
            }
        }

        log.debug("Пользователь {} с id {} успешно обновлен", oldUser.getName(), oldUser.getId());
        return oldUser;
    }
}
