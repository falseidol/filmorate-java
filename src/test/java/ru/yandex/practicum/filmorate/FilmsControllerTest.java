package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmsControllerTest {

    private static Film film;
    private static Film film2;
    private static LocalDate time;
    private static FilmService filmService;

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
        filmService = new FilmService();
    }

    @Test
    void shouldAddFilmToMap() {
        time = LocalDate.of(1900, 9, 1);
        filmService.addFilm(film);
        Assertions.assertEquals(1, filmService.getFilms().size());
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
        filmService.addFilm(film);
        filmService.updateFilm(film2);
        assertEquals(1, filmService.getFilms().size());
    }

    @Test
    void validateExistenceForPOSTTest() {
        filmService.addFilm(film);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
    }

    @Test
    void validateExistenceForPUTTest() {
        assertThrows(ValidationException.class, () -> filmService.updateFilm(film));
    }
}
