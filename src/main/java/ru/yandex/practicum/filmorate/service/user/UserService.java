package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Set;
import java.util.TreeSet;

@Service
@Slf4j
public class UserService {
    public User addFriend(int userId, int friendId, InMemoryUserStorage userStorage) {
        log.info("Добавление нового друга");
        User user = userStorage.findById(userId);
        User newFriend = userStorage.findById(friendId);

        log.debug("Добавление нового друга(id: {}) в список друзей пользовтеля(id {})", friendId, userId);
        if (user.getFriends() == null) {
            Set<Integer> friendList = new TreeSet<>();
            friendList.add(newFriend.getId());
            user.setFriends(friendList);
        } else {
            user.getFriends().add(newFriend.getId());
        }

        log.debug("Добавление пользователя(id: {}) в список друзей нового друга(id {})", userId, friendId);
        if (newFriend.getFriends() == null) {
            Set<Integer> friendList = new TreeSet<>();
            friendList.add(user.getId());
            newFriend.setFriends(friendList);
        } else {
            newFriend.getFriends().add(user.getId());
        }

        log.info("Списки друзей обновлены. Социализация прошла успешно");
        return newFriend;
    }

}
