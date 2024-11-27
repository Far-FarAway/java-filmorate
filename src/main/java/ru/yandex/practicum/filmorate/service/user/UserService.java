package ru.yandex.practicum.filmorate.service.user;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendList;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendListStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {
    public int addFriend(int userId, int friendId, FriendListStorage friendListStorage) {
        log.info("Добавление нового друга");
        return friendListStorage.addFriend(userId, friendId);
    }

    public List<User> getFriends(int userId, FriendListStorage friendListStorage) {
        log.info("Получение списка друзей пользователя с id {}", userId);
        List<User> list = new ArrayList<>();

        for(FriendList friend : friendListStorage.getFriends(userId)) {
            list.add(friend.getFriend());
        }

        return list;
    }

    public List<User> getCommonFriends(int userId, int friendId, FriendListStorage friendListStorage) {
        log.info("Получение списка общих друзей пользователя(id: {}) с другом(id: {})", userId, friendId);
        List<User> userFriendList = getFriends(userId, friendListStorage);
        List<User> friendListsOfFriend = getFriends(friendId, friendListStorage);

        List<User> commonFriendList = new ArrayList<>();

        for(User friend : userFriendList) {
            if (friendListsOfFriend.contains(friend)) {
                commonFriendList.add(friend);
            }
        }

        return commonFriendList;
    }

    public boolean deleteFriend(int userId, int friendId, FriendListStorage friendListStorage) {
        log.info("Удаление из списка друзей пользователя(id: {}) друга с id {}", userId, friendId);
        return friendListStorage.deleteFriend(userId, friendId);
    }
}
