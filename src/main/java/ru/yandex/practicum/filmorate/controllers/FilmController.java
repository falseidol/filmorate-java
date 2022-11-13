package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    private int makeID() {
        return ++id;
    }

    @GetMapping("/films")
    public HashMap<Integer, Film> getFilms() {
        log.info("Получен /GET запрос о выводе фильмов");
        return films;
    }

    @PostMapping(value = "/film/add")
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен /POST запрос добавление фильма");
        log.info("Проверка даты фильма");
        releaseDateShouldBeAfter28Dec1895Year(film);
        log.info("Проверка наличия в списке");
        validateExistenceForPOST(film);
        log.info("Проверка описания");
        validateDescription(film);
        log.info("Присваиваем id");
        film.setId(makeID());
        Film filmFromCreator = filmCreator(film);
        films.put(filmFromCreator.getId(), filmFromCreator);
        log.info("Фильм с названием " + filmFromCreator.getName() + " добавлен");
        return filmFromCreator;
    }

    Film filmCreator(Film film) {
        Film filmFromBuilder = Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();
        log.info("Объект Film создан '{}'", filmFromBuilder.getName());
        return filmFromBuilder;
    }

    @PutMapping(value = "/film/update")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен /PUT запрос обновление фильма");
        log.info("Проверка наличия в списке");
        validateExistenceForPUT(film);
        log.info("Проверка даты фильма");
        releaseDateShouldBeAfter28Dec1895Year(film);
        log.info("Проверка описания");
        validateDescription(film);
        Film filmFromCreator = filmCreator(film);
        films.put(filmFromCreator.getId(), filmFromCreator);
        log.info("Фильм с названием " + filmFromCreator.getName() + " обновлен");
        return film;
    }

    void releaseDateShouldBeAfter28Dec1895Year(Film film) {
        LocalDate filmsBirthDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(filmsBirthDate)) {
            log.info("Дата релиза фильма не может быть раньше 1895 года 28 декабря");
            throw new ValidationException("Дата релиза фильма не может быть раньше 1895 года 28 декабря");
        }
    }

    void validateExistenceForPOST(Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Id фильма '{}' ", film.getId());
            throw new ValidationException("Фильм с таким id уже существует!");
        }
    }

    void validateExistenceForPUT(Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.info("Id фильма '{}' ", film.getId());
            throw new ValidationException("Фильм с таким id осутствует!");
        }
    }

    void validateDescription(Film film) throws ValidationException {
        if (film.getDescription().length() > 200) {
            log.info("Размер описания '{}' ", film.getDescription().length());
            throw new ValidationException("Длина описания не может превышать 200 символов!");
        }
    }
}
