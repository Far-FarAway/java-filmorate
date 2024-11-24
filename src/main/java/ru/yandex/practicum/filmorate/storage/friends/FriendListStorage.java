package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.FriendList;

import java.util.Collection;
import java.util.List;

public interface FriendListStorage {
    boolean addFriend(int userId, int newFriendId);
    boolean deleteFriend(int userId, int friendId);
    Collection<FriendList> getFriends(int userId);
}
