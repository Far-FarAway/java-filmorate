package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getUsers();

    boolean postUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(int userId);

    User findById(int userId);
}
