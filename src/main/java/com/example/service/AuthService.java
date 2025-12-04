package com.example.service;

import com.example.models.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

@ApplicationScoped
@Transactional
public class AuthService {

    @Inject
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    // Метод для поиска пользователя по имени
    public Optional<User> findByUsername(String username) {
        return getCurrentSession()
                .createSelectionQuery("FROM User WHERE name = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    // Метод аутентификации
    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOptional = findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.checkPassword(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    // Метод регистрации
    public User register(User user) {
        // Проверка, существует ли уже пользователь с таким именем
        if (findByUsername(user.getName()).isPresent()) {
            throw new IllegalArgumentException("User with this username already exists");
        }

        // Хэширование пароля с помощью BCrypt
        String hashedPassword = at.favre.lib.crypto.bcrypt.BCrypt
                .withDefaults()
                .hashToString(12, user.getPassword().toCharArray());
        user.setPassword(hashedPassword);

        getCurrentSession().persist(user);
        return user;
    }

    // Метод проверки пароля
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return at.favre.lib.crypto.bcrypt.BCrypt.verifyer()
                .verify(plainPassword.toCharArray(), hashedPassword.toCharArray())
                .verified;
    }
}