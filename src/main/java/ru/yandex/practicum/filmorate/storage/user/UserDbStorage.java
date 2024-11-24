package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage{
    private final static String FIND_USER_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private final static String FIND_USERS_QUERY = "SELECT * FROM users";
    private final static String DELETE_USER_QUERY = "DELETE FROM users WHERE user_id = ?";
    private final static String UPDATE_USER_QUERY = "UPDATE users SET name = ?, login = ?," +
            " email = ?, birthday = ?, friend_status = ? " +
            "WHERE user_id = ?";
    private final static String INSERT_USER_QUERY = "INSERT INTO users(name, login, email, birthday, friend_status)" +
            " VALUES (?, ?, ?, ?, ?)";

    @Autowired
    public UserDbStorage(JdbcTemplate jdbc, @Qualifier("userRowMapper") RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> getUsers() {
        return findMany(FIND_USERS_QUERY);
    }

    public boolean postUser(User user) {
        return insert(INSERT_USER_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                Timestamp.from(user.getBirthday()),
                user.getFriendStatus().toString());
    }

    public boolean updateUser(User user) {
        User oldUser = findById(user.getId());
        return update(UPDATE_USER_QUERY,
                user.getName().isBlank() ? oldUser.getName() : user.getName(),
                user.getLogin().isBlank() ? oldUser.getLogin() : user.getLogin(),
                user.getEmail().isBlank() ? oldUser.getEmail() : user.getEmail(),
                user.getBirthday(),
                user.getFriendStatus().toString(),
                user.getId());
    }

    public boolean deleteUser(int userId) {
        return delete(DELETE_USER_QUERY, userId);
    }

    public User findById(int userId) {
        return findOne(FIND_USER_QUERY, userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
