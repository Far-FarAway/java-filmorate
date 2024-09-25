package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;

import java.time.LocalDate;
import java.time.Duration;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void shouldThrowExceptionWithEmptyFilmField() {
        Film film = new Film();
        FilmController controller = new FilmController();

        Throwable thrown = assertThrows(ConditionNotMetException.class, () -> controller.postFilm(film));
        assertTrue(thrown.getMessage().contains("Имя не долнжно быть пустым"));

        film.setName("F");
        thrown = assertThrows(ConditionNotMetException.class, () -> controller.postFilm(film));
        assertTrue(thrown.getMessage().contains("Описание не долнжно быть пустым"));

        film.setDescription("F");
        thrown = assertThrows(ConditionNotMetException.class, () -> controller.postFilm(film));
        assertTrue(thrown.getMessage().contains("Дата релиза не должна быть пустой"));

        film.setReleaseDate(LocalDate.now());
        thrown = assertThrows(ConditionNotMetException.class, () -> controller.postFilm(film));
        assertTrue(thrown.getMessage().contains("Продолжительность фильма не должна быть пустой"));
    }

    @Test
    void shouldThrowExceptionWithWrongDescription() {
        Film film = new Film();
        film.setName("F");
        film.setDescription("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
                "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFfFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
                "FFFFFFFFFFFFFFF");
        Throwable thrown = assertThrows(ConditionNotMetException.class, () -> new FilmController().postFilm(film));
        assertTrue(thrown.getMessage().contains("Длинна описания не должна быть больше 200 символов"));
    }

    @Test
    void shouldThrowExceptionWithWrongDate() {
        Film film = new Film();
        film.setName("F");
        film.setDescription("F");
        film.setReleaseDate(LocalDate.of(1000, 1, 1));

        Throwable thrown = assertThrows(ConditionNotMetException.class, () -> new FilmController().postFilm(film));
        assertTrue(thrown.getMessage().contains("Дата не может быть раньше 28.12.1985"));
    }

    @Test
    void shouldThrowExceptionWithWrongDuration() {
        Film film = new Film();
        film.setName("F");
        film.setDescription("F");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(Duration.ofMinutes((long) -1));

        Throwable thrown = assertThrows(ConditionNotMetException.class, () -> new FilmController().postFilm(film));
        assertTrue(thrown.getMessage().contains("Продолжительность фильма не должна быть меньше нуля"));
    }

    @Test
    void shouldThrowExceptionWithEmptyUserField() {
        User user = new User();
        UserController controller = new UserController();

        Throwable thrown = assertThrows(ConditionNotMetException.class, () -> controller.postUser(user));
        assertTrue(thrown.getMessage().contains("Логин не должен быть пустым"));

        user.setLogin("F");
        thrown = assertThrows(ConditionNotMetException.class, () -> controller.postUser(user));
        assertTrue(thrown.getMessage().contains("Имейл не должен быть пустым"));

        user.setEmail("F@F");
        thrown = assertThrows(ConditionNotMetException.class, () -> controller.postUser(user));
        assertTrue(thrown.getMessage().contains("Дата рождения не должна быть пустой"));
    }

    @Test
    void shouldThrowExceptionWithLoginWithSpace() {
        User user = new User();
        user.setLogin("F F");
        Throwable thrown = assertThrows(ConditionNotMetException.class, () -> new UserController().postUser(user));
        assertTrue(thrown.getMessage().contains("Логин не должен содержать пробелы"));
    }

    @Test
    void shouldThrowExceptionWithDuplicateLogin() {
        User user1 = new User();
        UserController controller = new UserController();
        user1.setLogin("F");
        user1.setEmail("F@F");
        user1.setBirthday(LocalDate.now().minusDays(3));
        controller.postUser(user1);
        User user2 = new User();
        user2.setLogin("F");
        Throwable thrown = assertThrows(ConditionNotMetException.class, () -> controller.postUser(user2));
        assertTrue(thrown.getMessage().contains("Логин уже используется"));
    }

    @Test
    void shouldThrowExceptionWithEmailWithoutSpecialSign() {
        User user = new User();
        user.setLogin("F");
        user.setEmail("FFF");
        Throwable thrown = assertThrows(ConditionNotMetException.class, () -> new UserController().postUser(user));
        assertTrue(thrown.getMessage().contains("Имейл должен содержать символ \"@\""));
    }

    @Test
    void shouldThrowExceptionWithDuplicateEmail() {
        User user1 = new User();
        UserController controller = new UserController();
        user1.setLogin("F");
        user1.setEmail("F@F");
        user1.setBirthday(LocalDate.now().minusDays(3));
        controller.postUser(user1);
        User user2 = new User();
        user2.setLogin("FFF");
        user2.setEmail("F@F");
        Throwable thrown = assertThrows(ConditionNotMetException.class, () -> controller.postUser(user2));
        assertTrue(thrown.getMessage().contains("Имейл уже используется"));
    }

    @Test
    void shouldThrowExceptionWithWrongBirthday() {
        User user = new User();
        user.setLogin("F");
        user.setEmail("F@F");
        user.setBirthday(LocalDate.now().plusDays(3));
        Throwable thrown = assertThrows(ConditionNotMetException.class, () -> new UserController().postUser(user));
        assertTrue(thrown.getMessage().contains("Дата рождения не должна быть в будущем"));
    }
}
