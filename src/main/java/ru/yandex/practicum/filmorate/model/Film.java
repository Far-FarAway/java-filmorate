package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.yandex.practicum.filmorate.LocalDateToInstantDeserializer;
import ru.yandex.practicum.filmorate.annotation.OnCreate;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
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
    @JsonDeserialize(using = LocalDateToInstantDeserializer.class)
    private Instant releaseDate;
    @PositiveOrZero
    int duration;
    private String genre;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    private String rating;
}