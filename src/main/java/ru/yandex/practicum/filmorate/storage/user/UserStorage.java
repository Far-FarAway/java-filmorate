package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User postUser(User user);

    User updateUser(User user);

    boolean deleteUser(int userId);

    User findById(int userId);
}
