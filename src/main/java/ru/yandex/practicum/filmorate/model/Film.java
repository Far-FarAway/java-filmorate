package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.annotation.OnCreate;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class Film {
    @PositiveOrZero
    private int id;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull(groups = {OnCreate.class})
    @PastOrPresent
    private Instant releaseDate;
    @PositiveOrZero
    int duration;
    private String genre;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    private String rating;
}