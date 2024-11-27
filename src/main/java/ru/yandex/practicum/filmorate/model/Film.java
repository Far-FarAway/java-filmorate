package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.annotation.OnCreate;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    @PositiveOrZero
    private int id;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    private String name;
    @Size(max = 200)
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    private String description;
    @NotNull(groups = {OnCreate.class})
    @PastOrPresent
    private LocalDate releaseDate;
    @PositiveOrZero
    int duration;
    private List<Genre> genres;
    private Mpa mpa;
}