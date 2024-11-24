package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendList;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FriendListRowMapper implements RowMapper<FriendList> {
    private final UserStorage storage;

    @Override
    public FriendList mapRow(ResultSet results, int rowNum) throws SQLException {
        FriendList friendList = new FriendList();
        friendList.setUser(storage.findById(results.getInt("user_id")));
        friendList.setFriends(storage.findById(results.getInt("friend_id")));

        return friendList;
    }
}
