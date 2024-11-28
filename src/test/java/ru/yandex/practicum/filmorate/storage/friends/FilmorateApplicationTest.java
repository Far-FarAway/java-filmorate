package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.film.FilmDbService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.filmsgenres.FilmsGenresDbStorage;
import ru.yandex.practicum.filmorate.storage.filmsgenres.FilmsGenresRowMapper;
import ru.yandex.practicum.filmorate.storage.filmslikes.FilmsLikesDbStorage;
import ru.yandex.practicum.filmorate.storage.filmslikes.FilmsLikesRowMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRowMapper.class, UserDbStorage.class, FriendListDbStorage.class, FriendListRowMapper.class,
        FilmRowMapper.class, FilmDbService.class, FilmsLikesDbStorage.class, FilmsLikesRowMapper.class,
        GenreRowMapper.class, GenreDbStorage.class, MpaRowMapper.class, MpaDbStorage.class,
        FilmDbStorage.class, FilmsGenresDbStorage.class, FilmsGenresRowMapper.class})
class FilmorateApplicationTest {
    private final UserDbStorage userStorage;
    private final FriendListDbStorage friendStorage;
    private final FilmsLikesDbStorage likesStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;
    private final FilmDbService filmService;
    private final JdbcTemplate jdbc;
    User user1;
    User user2;
    Film film1;
    Film film2;
    Genre fantasy;
    Genre comedy;
    Mpa g;
    Mpa pg;

    @BeforeEach
    public void before() {
        fantasy = new Genre();
        fantasy.setName("fantasy");
        genreStorage.postGenre(fantasy);

        comedy = new Genre();
        comedy.setName("comedy");
        genreStorage.postGenre(comedy);

        g = new Mpa();
        g.setName("G");
        mpaStorage.postMpa(g);

        pg = new Mpa();
        pg.setName("PG");
        mpaStorage.postMpa(pg);

        user1 = new User();
        user1.setName("test1");
        user1.setLogin("testUser1");
        user1.setEmail("ewewewe@yandex.ru");
        user1.setBirthday(LocalDate.now());
        user1.setFriendStatus(FriendStatus.UNCONFIRMED);

        user2 = new User();
        user2.setName("test2");
        user2.setLogin("testUser2");
        user2.setEmail("eqweqwe@yandex.ru");
        user2.setBirthday(LocalDate.now());
        user2.setFriendStatus(FriendStatus.UNCONFIRMED);

        film1 = new Film();
        film1.setName("testFilm1");
        film1.setDescription("1Test1");
        film1.setGenres(new ArrayList<>());
        film1.getGenres().add(fantasy);
        film1.setReleaseDate(LocalDate.now());
        film1.setMpa(g);
        film1.setDuration(230);

        film2 = new Film();
        film2.setName("testFilm2");
        film2.setDescription("2TestTest2");
        film2.setGenres(new ArrayList<>());
        film2.getGenres().add(comedy);
        film2.setReleaseDate(LocalDate.now());
        film2.setMpa(pg);
        film1.setDuration(20);

        jdbc.execute("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1");
        jdbc.execute("ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1");
    }

    @Test
    public void testPostUser() {
        assertThat(userStorage.postUser(user1)).isEqualTo(user1);
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
        updatedUser.setBirthday(LocalDate.now());
        updatedUser.setFriendStatus(FriendStatus.UNCONFIRMED);

        userStorage.postUser(user1);
        userStorage.postUser(user2);

        userStorage.updateUser(updatedUser);

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

        assertThat(friendStorage.addFriend(1, 2)).isEqualTo(-3);
        assertThat(friendStorage.addFriend(2, 1)).isEqualTo(-3);
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

//        /*boolean isContains = */friendStorage.getFriends(2);

        assertThat(friendStorage.getFriends(2).size()).isEqualTo(0);
    }

    @Test
    public void testPostFilm() {
        assertThat(filmService.postFilm(film1)).isEqualTo(film1);
    }

    @Test
    public void testFindFilmById() {
        filmService.postFilm(film1);

        System.out.println(filmService.getFilms());

        assertThat(filmService.getFilm(1)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void testFindFilms() {
        filmService.postFilm(film2);
        filmService.postFilm(film1);

        assertThat(filmService.getFilms().size() == 2).isEqualTo(true);
    }

    @Test
    public void testUpdateFilm() {
        Film updatedFilm = new Film();
        updatedFilm.setId(2);
        updatedFilm.setName("updatedData");
        updatedFilm.setDescription("updatedAtad");
        updatedFilm.setReleaseDate(LocalDate.now());
        updatedFilm.setDuration(1220);

        filmService.postFilm(film1);
        filmService.postFilm(film2);

        filmService.updateFilm(updatedFilm);

        assertThat(filmService.getFilm(updatedFilm.getId()))
                .hasFieldOrPropertyWithValue("name", "updatedData");
    }

    @Test
    public void testDeleteFilm() {
        filmService.postFilm(film1);
        filmService.postFilm(film2);

        filmService.deleteFilm(1);

        assertThat(filmService.getFilms().size() == 1).isEqualTo(true);
    }

    @Test
    public void testAddLike() {
        filmService.postFilm(film1);
        userStorage.postUser(user1);

        assertThat(likesStorage.addLike(1, 1)).isEqualTo(-3);
    }

    @Test
    public void testGetLikes() {
        filmService.postFilm(film1);
        userStorage.postUser(user1);
        userStorage.postUser(user2);

        likesStorage.addLike(1, 1);
        likesStorage.addLike(1, 2);

        assertThat(likesStorage.getLikes().size() == 2).isEqualTo(true);
    }

    @Test
    public void testGetLikesByFilm() {
        filmService.postFilm(film1);
        filmService.postFilm(film2);
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
        filmService.postFilm(film1);
        userStorage.postUser(user1);
        userStorage.postUser(user2);

        likesStorage.addLike(1, 1);
        likesStorage.addLike(1, 2);

        assertThat(likesStorage.deleteLike(1, 1)).isEqualTo(true);
        assertThat(likesStorage.getLikesByFilm((1)).size() == 1).isEqualTo(true);
    }
}
