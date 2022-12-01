package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film putLike(int filmId, int userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new ObjectNotFoundException("Фильма с таким ид не существует.");
        }
        Film film = filmStorage.getFilmById(filmId);
        film.getLikes().add(userId);
        log.info("Лайк добавлен.");
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteLike(int filmId, int userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new ObjectNotFoundException("Фильма с таким ид не существует.");
        }
        if (!filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            throw new ObjectNotFoundException("Лайк отсуствует.");
        }
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
        log.info("Лайк удален.");
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Список популярных фильмов отправлен");
        return filmStorage.findAll().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
