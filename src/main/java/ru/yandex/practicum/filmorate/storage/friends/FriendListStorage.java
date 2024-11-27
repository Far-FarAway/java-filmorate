package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.FriendList;

import java.util.Collection;
import java.util.List;

public interface FriendListStorage {
    int addFriend(int userId, int newFriendId);

    boolean deleteFriend(int userId, int friendId);

    List<FriendList> getFriends(int userId);

    boolean deleteUser(int userId);
}
