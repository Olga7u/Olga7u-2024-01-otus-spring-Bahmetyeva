package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JpaGenreRepository implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = entityManager.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Set<Genre> findAllByIds(Set<Long> ids) {
        TypedQuery<Genre> query = entityManager.createQuery(
                "select g from Genre g where g.id in (:ids)", Genre.class);
        query.setParameter("ids", ids);
        List<Genre> genreList = query.getResultList();
        return new HashSet<>(genreList);
    }
}
