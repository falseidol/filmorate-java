package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Film addFilm(Film film);

    Collection<Film> findAll();

    Film updateFilm(Film film);

    Film getFilmById(int id);

    Film deleteById(int id);

    Map<Integer,Film> getFilms();
}
