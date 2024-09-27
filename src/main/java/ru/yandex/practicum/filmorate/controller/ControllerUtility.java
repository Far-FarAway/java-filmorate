package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ControllerUtility {

    public static boolean isDuplicate(Map<Integer, User> map, String value) {
        Optional<User> duplicate = map.values().stream()
                .filter(person -> {
                    if (value.contains("@")) {
                        return person.getEmail().equals(value);
                    } else {
                        return person.getLogin().equals(value);
                    }
                }).findAny();

        return duplicate.isPresent();
    }

    public static int getNextId(Set<Integer> list) {
        int currentMaxId = list.stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
