package com.example.pagila_api.service;

import com.example.pagila_api.exception.ResourceNotFoundException;
import com.example.pagila_api.model.Film;
import com.example.pagila_api.repository.FilmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Page<Film> getAllFilms(Pageable pageable) {
        return filmRepository.findAll(pageable);
    }

    public Film getFilmById(Integer id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
    }

    public Film getFilmByIdWithActors(Integer id) {
        return filmRepository.findByIdWithActors(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
    }

    @Transactional
    public Film createFilm(Film film) {
        return filmRepository.save(film);
    }

    @Transactional
    public Film updateFilm(Integer id, Film updatedFilm) {
        Film existingFilm = getFilmById(id);

        existingFilm.setTitle(updatedFilm.getTitle());
        existingFilm.setDescription(updatedFilm.getDescription());
        existingFilm.setReleaseYear(updatedFilm.getReleaseYear());
        existingFilm.setLanguageId(updatedFilm.getLanguageId());
        existingFilm.setOriginalLanguageId(updatedFilm.getOriginalLanguageId());
        existingFilm.setRentalDuration(updatedFilm.getRentalDuration());
        existingFilm.setRentalRate(updatedFilm.getRentalRate());
        existingFilm.setLength(updatedFilm.getLength());
        existingFilm.setReplacementCost(updatedFilm.getReplacementCost());
        existingFilm.setRating(updatedFilm.getRating());
        existingFilm.setSpecialFeatures(updatedFilm.getSpecialFeatures());

        return filmRepository.save(existingFilm);
    }

    @Transactional
    public void deleteFilm(Integer id) {
        if (!filmRepository.existsById(id)) {
            throw new ResourceNotFoundException("Film not found with ID: " + id);
        }
        filmRepository.deleteById(id);
    }

    public Page<Film> searchFilmsByTitle(String title, Pageable pageable) {
        return filmRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
}