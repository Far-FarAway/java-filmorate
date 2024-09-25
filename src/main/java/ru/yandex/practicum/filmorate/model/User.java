package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;


@Data
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
