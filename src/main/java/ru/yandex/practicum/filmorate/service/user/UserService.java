package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FriendList;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendListStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {
    public boolean addFriend(int userId, int friendId, FriendListStorage friendListStorage) {
        log.info("Добавление нового друга");
        return friendListStorage.addFriend(userId, friendId);
    }

    public Map<String, List<String>> getFriends(int userId, FriendListStorage friendListStorage) {
        log.info("Получение списка друзей пользователя с id {}", userId);
        Map<String, List<String>> friendList = new HashMap<>();
        List<String> list = new ArrayList<>();

        for(FriendList friend : friendListStorage.getFriends(userId)) {
            list.add(friend.getFriend().getLogin());
            friendList.put(friend.getUser().getLogin(), list);
        }

        return friendList;
    }

    public List<String> getCommonFriends(int userId, int friendId, FriendListStorage friendListStorage) {
        log.info("Получение списка общих друзей пользователя(id: {}) с другом(id: {})", userId, friendId);
        List<String> userFriendList = getFriends(userId, friendListStorage).get(userId);
        List<String> friendListsOfFriend = getFriends(friendId, friendListStorage).get(friendId);

        List<String> commonFriendList = new ArrayList<>();
        for(String friend : friendListsOfFriend) {
            if (userFriendList.contains(friend)) {
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
