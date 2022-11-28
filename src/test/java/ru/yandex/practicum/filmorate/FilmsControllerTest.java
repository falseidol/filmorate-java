package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmsControllerTest {

    private static Film film;
    private static Film film2;

    private static Film film3;
    private static LocalDate time;
    private static FilmStorage filmStorage;

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
        filmStorage = new InMemoryFilmStorage();
    }

    @Test
    void shouldAddFilmToMap() {
        time = LocalDate.of(1900, 9, 1);
        filmStorage.addFilm(film);
        Assertions.assertEquals(1, filmStorage.getFilms().size());
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
        filmStorage.addFilm(film);
        filmStorage.updateFilm(film2);
        assertEquals(1, filmStorage.getFilms().size());
    }

    @Test
    void validateExistenceForPOSTTest() {
        filmStorage.addFilm(film);
        assertThrows(ValidationException.class, () -> filmStorage.addFilm(film));
    }

    @Test
    void validateExistenceForPUTTest() {
        assertThrows(NullPointerException.class, () -> filmStorage.updateFilm(film3));
    }
}
