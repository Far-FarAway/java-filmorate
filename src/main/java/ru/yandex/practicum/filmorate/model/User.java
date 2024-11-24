package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.yandex.practicum.filmorate.LocalDateToInstantDeserializer;
import ru.yandex.practicum.filmorate.annotation.OnCreate;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;


@Data
public class User {
    @PositiveOrZero
    private int id;
    @Email
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    private String email;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    private String login;
    private String name;
    @PastOrPresent
    @JsonDeserialize(using = LocalDateToInstantDeserializer.class)
    private Instant birthday;
    private FriendStatus friendStatus;
}
