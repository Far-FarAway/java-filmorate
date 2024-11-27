package ru.yandex.practicum.filmorate.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendListStorage friendStorage;

    public User getUser(int userId) {
        return userStorage.findById(userId);
    }

    public boolean addFriend(int userId, int friendId) {
        log.info("Добавление нового друга");
        User user = new User();
        user.setId(userId);
        if (userStorage.getUsers().contains(user)) {
            return friendStorage.addFriend(userId, friendId) == -3;
        } else {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    public List<User> getFriends(int userId) {
        log.info("Получение списка друзей пользователя с id {}", userId);
        List<User> list = new ArrayList<>();

        for(FriendList friend : friendStorage.getFriends(userId)) {
            list.add(friend.getFriend());
        }

        return list;
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        log.info("Получение списка общих друзей пользователя(id: {}) с другом(id: {})", userId, friendId);
        List<User> userFriendList = getFriends(userId);
        List<User> friendListsOfFriend = getFriends(friendId);

        List<User> commonFriendList = new ArrayList<>();

        for(User friend : userFriendList) {
            if (friendListsOfFriend.contains(friend)) {
                commonFriendList.add(friend);
            }
        }

        return commonFriendList;
    }

    public boolean deleteFriend(int userId, int friendId) {
        log.info("Удаление из списка друзей пользователя(id: {}) друга с id {}", userId, friendId);
        return friendStorage.deleteFriend(userId, friendId);
    }

    public boolean deleteUser(int userId) {
        friendStorage.deleteUser(userId);
        return userStorage.deleteUser(userId);
    }
}
