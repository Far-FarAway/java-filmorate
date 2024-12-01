package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@RestController
@RequestMapping("genres")
@AllArgsConstructor
public class GenreController {
    private final GenreStorage genreStorage;

    @DeleteMapping("/{id}")
    public boolean deleteGenre(@PathVariable int id) {
        return genreStorage.deleteGenre(id);
    }

    @GetMapping
    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        return genreStorage.getGenre(id);
    }

    @PostMapping
    public Genre postGenre(@RequestBody Genre genre) {
        return genreStorage.postGenre(genre);
    }
}
