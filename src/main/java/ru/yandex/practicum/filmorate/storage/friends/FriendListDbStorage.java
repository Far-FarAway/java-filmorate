package ru.yandex.practicum.filmorate.storage.friends;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendList;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.*;

@Repository
public class FriendListDbStorage extends BaseRepository<FriendList> implements FriendListStorage {
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_FRIENDS_QUERY = "SELECT * FROM friends WHERE user_id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM friends WHERE user_id = ?";

    public FriendListDbStorage(JdbcTemplate jdbc, @Qualifier("friendListRowMapper") RowMapper<FriendList> mapper) {
        super(jdbc, mapper);
    }

    public int addFriend(int userId, int newFriendId) {
        return insert(ADD_FRIEND_QUERY, userId, newFriendId);
    }

    public boolean deleteFriend(int userId, int friendId) {
        return delete(DELETE_FRIEND_QUERY, userId, friendId);
    }

    public boolean deleteUser(int userId) {
        return delete(DELETE_USER_QUERY, userId);
    }

    public List<FriendList> getFriends(int userId) {
        return findMany(FIND_FRIENDS_QUERY, userId);
    }
}
