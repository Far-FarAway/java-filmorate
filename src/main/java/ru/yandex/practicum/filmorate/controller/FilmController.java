package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.yandex.practicum.filmorate.annotation.OnCreate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmslikes.FilmsLikesStorage;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    FilmStorage filmStorage;
    FilmsLikesStorage likesStorage;
    FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/likes")
    public Map<String, List<String>> getLikes() {
        return filmService.getLikes(likesStorage);
    }

    @GetMapping("/likes/{filmId}")
    public Map<String, List<String>> getLikes(@PathVariable int filmId) {
        return filmService.getLikesByFilm(filmId, likesStorage);
    }

    @GetMapping("/popular")
    public Set<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count, filmStorage, likesStorage);
    }

    @PostMapping
    @Validated({OnCreate.class})
    public Film postFilm(@Valid @RequestBody Film film) {
        return filmStorage.postFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public int addLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.addLike(filmId, userId, likesStorage);
    }

    @PutMapping
    public boolean updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping("/{filmId}")
    public boolean deleteFilm(@PathVariable int filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public boolean deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.deleteLike(filmId, userId, likesStorage);
    }

    @GetMapping("/genres")
    public Set<String> getGenres() {
        return filmService.getGenres(filmStorage);
    }

    @GetMapping("/genres/{id}")
    public String getGenreByFilm(@PathVariable int id) {
        return filmService.getGenreByFilm(id, filmStorage);
    }

    @GetMapping("/mpa")
    public Set<String> getRating() {
        return filmService.getRating(filmStorage);
    }

    @GetMapping("/mpa/{id}")
    public String getRatingByFilm(@PathVariable int id) {
        return filmService.getRatingByFilm(id, filmStorage);
    }


}
