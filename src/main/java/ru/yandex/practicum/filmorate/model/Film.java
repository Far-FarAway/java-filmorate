package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    int id;
    String name;
    String description;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    LocalDate releaseDate;
    Duration duration;
}
