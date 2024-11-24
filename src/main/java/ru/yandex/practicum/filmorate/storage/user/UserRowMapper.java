package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Qualifier("userRowMapper")
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet results, int roeNum) throws SQLException {
        User user = new User();
        user.setId(results.getInt("user_id"));
        user.setName(results.getString("name"));
        user.setLogin(results.getString("login"));
        user.setEmail(results.getString("email"));
        user.setBirthday(results.getTimestamp("birthday").toInstant());
        user.setFriendStatus(FriendStatus.valueOf(results.getString("friend_status")));

        return user;
    }
}
