package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User getUser(int userId);

    boolean addFriend(int userId, int friendId);

    List<User> getFriends(int userId);

    List<User> getCommonFriends(int userId, int friendId);

    boolean deleteFriend(int userId, int friendId);

    boolean deleteUser(int userId);

}
