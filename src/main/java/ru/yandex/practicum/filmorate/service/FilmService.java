package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

@Service
@Slf4j
public class FilmService {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    private int makeID() {
        return ++id;
    }

    public Collection<Film> getFilms() {
        return films.values();
    }

    public Film addFilm(Film film) {
        log.info("Проверка наличия в списке");
        validateExistenceForPOST(film);
        log.info("Присваиваем id");
        film.setId(makeID());
        Film filmFromCreator = filmCreator(film);
        films.put(filmFromCreator.getId(), filmFromCreator);
        log.info("Фильм с названием " + filmFromCreator.getName() + " добавлен");
        return film;
    }

    private Film filmCreator(Film film) {
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

    public Film updateFilm(Film film) {
        log.info("Проверка наличия в списке");
        validateExistenceForPUT(film);
        Film filmFromCreator = filmCreator(film);
        films.put(filmFromCreator.getId(), filmFromCreator);
        log.info("Фильм с названием " + filmFromCreator.getName() + " обновлен");
        return film;
    }

    private void validateExistenceForPOST(Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Id фильма '{}' ", film.getId());
            throw new ValidationException("Фильм с таким id уже существует!");
        }
    }

    private void validateExistenceForPUT(Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.info("Id фильма '{}' ", film.getId());
            throw new ValidationException("Фильм с таким id осутствует!");
        }
    }
}
