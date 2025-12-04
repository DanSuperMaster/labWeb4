package com.example.service;


import com.example.models.Dot;
import com.example.models.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class UserService {

    @Inject
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void createUser(User user) {
        getCurrentSession().persist(user);
    }

    public List<User> getAllUsers() {
        return getCurrentSession().createSelectionQuery("FROM users", User.class)
                .getResultList();
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(getCurrentSession().get(User.class, id));
    }

    public void removeUser(Long id) {
        final User user = findById(id).get();
        getCurrentSession().remove(user);
    }
}
