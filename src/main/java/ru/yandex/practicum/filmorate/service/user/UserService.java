package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    public User addFriend(int userId, int friendId, UserStorage storage) {
        log.info("Добавление нового друга");
        User user = storage.findById(userId);
        User newFriend = storage.findById(friendId);

        log.debug("Добавление нового друга(id: {}) в список друзей пользовтеля(id {})", friendId, userId);
        user.getFriends().add(newFriend.getId());

        log.debug("Добавление пользователя(id: {}) в список друзей нового друга(id {})", userId, friendId);
        newFriend.getFriends().add(user.getId());

        log.info("Списки друзей обновлены. Социализация прошла успешно");
        return newFriend;
    }

    public Collection<Integer> getFriends(int userId, UserStorage storage) {
        log.info("Получение списка друзей пользователя с id {}", userId);
        return storage.findById(userId).getFriends();
    }

    public Collection<Integer> getCommonFriends(int userId, int friendId, UserStorage storage) {
        log.info("Получение списка общих друзей пользователя(id: {}) с другом(id: {})", userId, friendId);
        User user = storage.findById(userId);
        User friend = storage.findById(friendId);
        Set<Integer> friendFriendList = friend.getFriends();
        return user.getFriends().stream().filter(friendFriendList::contains).toList();
    }

    public Collection<Integer> deleteFriend(int userId, int friendId, UserStorage storage) {
        log.info("Удаление из списка друзей пользователя(id: {}) друга с id {}", userId, friendId);
        Set<Integer> userFriendList = storage.findById(userId).getFriends();
        Set<Integer> friendFriendList = storage.findById(friendId).getFriends();
        log.info("Пользователь(id: {}) успешно удален из списка друзей", friendId);
        userFriendList.remove(friendId);
        friendFriendList.remove(userId);
        return userFriendList;

    }


}
