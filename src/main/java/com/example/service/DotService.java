package com.example.service;


import com.example.models.Dot;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class DotService {

    @Inject
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void createDot(Dot dot) {
        getCurrentSession().persist(dot);
    }

    public Optional<Dot> findById(Long id) {
        return Optional.ofNullable(getCurrentSession().get(Dot.class, id));
    }

    public List<Dot> getAllDots() {
        return getCurrentSession().createSelectionQuery("FROM dots", Dot.class)
                .getResultList();
    }

    public void removeDot(Long id) {
        final Dot dot = findById(id).get();
        getCurrentSession().remove(dot);
    }




}
