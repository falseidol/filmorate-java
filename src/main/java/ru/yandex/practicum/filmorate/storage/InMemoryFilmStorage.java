package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    private int makeID() {
        return ++id;
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

    @Override
    public Collection<Film> findAll() {
        return films.values();
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

    @Override
    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильма с таким ид не существует.");
        }
        log.info("Выводим фильм.");
        return films.get(id);
    }

    @Override
    public Film deleteById(int id) {
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильма с таким ид не существует.");
        }
        Film film = films.get(id);
        films.remove(id);
        log.info("Фильм с ид" + id + " удалён.");
        return film;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }


    public void validateExistenceForPOST(Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Id фильма '{}' ", film.getId());
            throw new ValidationException("Фильм с таким id уже существует!");
        }
    }

    public void validateExistenceForPUT(Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Id фильма '{}' ", film.getId());
            throw new ObjectNotFoundException("Фильм с таким id осутствует!");
        }
    }
}
