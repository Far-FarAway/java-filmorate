package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FriendList;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.filmslikes.FilmsLikesDbStorage;
import ru.yandex.practicum.filmorate.storage.filmslikes.FilmsLikesRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRowMapper.class, UserDbStorage.class, FriendListDbStorage.class, FriendListRowMapper.class,
         FilmRowMapper.class, FilmDbStorage.class, FilmsLikesDbStorage.class, FilmsLikesRowMapper.class})
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FriendListDbStorage friendStorage;
    private final FilmDbStorage filmStorage;
    private final FilmsLikesDbStorage likesStorage;
    private final JdbcTemplate jdbc;
    User user1;
    User user2;
    Film film1;
    Film film2;

    @BeforeEach
    public void before() {
        user1 = new User();
        user1.setName("test1");
        user1.setLogin("testUser1");
        user1.setEmail("ewewewe@yandex.ru");
        user1.setBirthday(Instant.now());
        user1.setFriendStatus(FriendStatus.UNCONFIRMED);

        user2 = new User();
        user2.setName("test2");
        user2.setLogin("testUser2");
        user2.setEmail("eqweqwe@yandex.ru");
        user2.setBirthday(Instant.now());
        user2.setFriendStatus(FriendStatus.UNCONFIRMED);

        film1 = new Film();
        film1.setName("testFilm1");
        film1.setDescription("1Test1");
        film1.setGenre("Fantasy");
        film1.setReleaseDate(Instant.now());
        film1.setRating("G");
        film1.setDuration(230);

        film2 = new Film();
        film2.setName("testFilm2");
        film2.setDescription("2TestTest2");
        film2.setGenre("Comedy");
        film2.setReleaseDate(Instant.now());
        film2.setRating("PG");
        film1.setDuration(20);

        jdbc.execute("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1");
        jdbc.execute("ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1");
    }

    @Test
    public void testPostUser() {
        assertThat(userStorage.postUser(user1)).isEqualTo(true);
    }

    @Test
    public void testFindUserById() {
        userStorage.postUser(user2);

        assertThat(userStorage.findById(1)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void testFindUsers() {
        userStorage.postUser(user2);
        userStorage.postUser(user1);

        assertThat(userStorage.getUsers().size() == 2).isEqualTo(true);
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setId(2);
        updatedUser.setName("updatedData");
        updatedUser.setLogin("updatedAtad");
        updatedUser.setEmail("dsadsadsasadads@yandex.ru");
        updatedUser.setBirthday(Instant.now());
        updatedUser.setFriendStatus(FriendStatus.UNCONFIRMED);

        userStorage.postUser(user1);
        userStorage.postUser(user2);

        assertThat(userStorage.updateUser(updatedUser)).isEqualTo(true);
        assertThat(userStorage.findById(updatedUser.getId()))
                .hasFieldOrPropertyWithValue("login", "updatedAtad");
    }

    @Test
    public void testDeleteUser() {
        userStorage.postUser(user2);
        userStorage.postUser(user1);

        userStorage.deleteUser(1);

        assertThat(userStorage.getUsers().size() == 1).isEqualTo(true);
    }

    @Test
    public void testAddFriend() {
        userStorage.postUser(user1);
        userStorage.postUser(user2);

        assertThat(friendStorage.addFriend(1, 2)).isEqualTo(true);
        assertThat(friendStorage.addFriend(2, 1)).isEqualTo(true);
    }

    @Test
    public void testGetFriends() {
        userStorage.postUser(user1);
        userStorage.postUser(user2);

        friendStorage.addFriend(1, 2);
        friendStorage.addFriend(2, 1);

        FriendList friendList = friendStorage.getFriends(1).getFirst();

        assertThat(friendList.getUser()).hasFieldOrPropertyWithValue("id", 1);
        assertThat(friendList.getFriend()).hasFieldOrPropertyWithValue("id", 2);
    }

    @Test
    public void testDeleteFriend() {
        userStorage.postUser(user1);
        userStorage.postUser(user2);

        friendStorage.addFriend(1, 2);
        friendStorage.addFriend(2, 1);

        friendStorage.deleteFriend(2, 1);

        assertThat(friendStorage.getFriends(2).contains(user1)).isEqualTo(false);
    }

    @Test
    public void testPostFilm() {
        assertThat(filmStorage.postFilm(film1)).isEqualTo(true);
    }

    @Test
    public void testFindFilmById() {
        filmStorage.postFilm(film1);

        System.out.println(filmStorage.getFilms());

        assertThat(filmStorage.findById(1)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void testFindFilms() {
        filmStorage.postFilm(film2);
        filmStorage.postFilm(film1);

        assertThat(filmStorage.getFilms().size() == 2).isEqualTo(true);
    }

    @Test
    public void testUpdateFilm() {
        Film updatedFilm = new Film();
        updatedFilm.setId(2);
        updatedFilm.setName("updatedData");
        updatedFilm.setDescription("updatedAtad");
        updatedFilm.setGenre("Romantic");
        updatedFilm.setReleaseDate(Instant.now());
        updatedFilm.setRating("G");
        updatedFilm.setDuration(1220);

        filmStorage.postFilm(film1);
        filmStorage.postFilm(film2);

        assertThat(filmStorage.updateFilm(updatedFilm)).isEqualTo(true);
        assertThat(filmStorage.findById(updatedFilm.getId()))
                .hasFieldOrPropertyWithValue("genre", "Romantic");
    }

    @Test
    public void testDeleteFilm() {
        filmStorage.postFilm(film1);
        filmStorage.postFilm(film2);

        filmStorage.deleteFilm(1);

        assertThat(filmStorage.getFilms().size() == 1).isEqualTo(true);
    }

    @Test
    public void testAddLike() {
        filmStorage.postFilm(film1);
        userStorage.postUser(user1);

        assertThat(likesStorage.addLike(1, 1)).isEqualTo(true);
    }

    @Test
    public void testGetLikes() {
        filmStorage.postFilm(film1);
        userStorage.postUser(user1);
        userStorage.postUser(user2);

        likesStorage.addLike(1, 1);
        likesStorage.addLike(1, 2);

        assertThat(likesStorage.getLikes().size() == 2).isEqualTo(true);
    }

    @Test
    public void testGetLikesByFilm() {
        filmStorage.postFilm(film1);
        filmStorage.postFilm(film2);
        userStorage.postUser(user1);
        userStorage.postUser(user2);
        userStorage.postUser(user1);

        likesStorage.addLike(1, 1);
        likesStorage.addLike(1, 2);
        likesStorage.addLike(2, 3);

        assertThat(likesStorage.getLikesByFilm(1).size() == 2).isEqualTo(true);
        assertThat(likesStorage.getLikesByFilm(2).size() == 1).isEqualTo(true);
    }

    @Test
    public void testDeleteLike() {
        filmStorage.postFilm(film1);
        userStorage.postUser(user1);
        userStorage.postUser(user2);

        likesStorage.addLike(1, 1);
        likesStorage.addLike(1, 2);

        assertThat(likesStorage.deleteLike(1, 1)).isEqualTo(true);
        assertThat(likesStorage.getLikesByFilm((1)).size() == 1).isEqualTo(true);
    }
}