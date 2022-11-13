package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
public class FilmsControllerTest {

    static Film film;
    static Film film2;
    static LocalDate time;
    static FilmController filmController = new FilmController();

    @BeforeEach
    void init() {
        time = LocalDate.of(2002, 8, 2);
        film = Film.builder()
                .id(1)
                .name("bebrachast'два")
                .description("бебру потеряли")
                .releaseDate(LocalDate.of(2022, 3, 14))
                .duration(20)
                .build();
    }

    @AfterEach
    void cleaner() {
        filmController.getFilms().clear();
    }

    @Test
    void shouldAddFilmToMap() {
        time = LocalDate.of(2002, 8, 2);
        filmController.addFilm(film);
        Assertions.assertEquals(1, filmController.getFilms().size());
    }

    @Test
    void shouldUpdateFilmInMap() {
        time = LocalDate.of(2002, 8, 2);
        film2 = Film.builder()
                .id(1)
                .name("test")
                .description("test")
                .releaseDate(time)
                .duration(20)
                .build();
        filmController.addFilm(film);
        filmController.updateFilm(film2); // they had same id
        Assertions.assertEquals("test", filmController.getFilms().get(1).getName());
    }
}
