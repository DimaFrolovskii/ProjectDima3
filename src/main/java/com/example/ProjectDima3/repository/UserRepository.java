package com.example.ProjectDima3.repository;

import com.example.ProjectDima3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Этот метод критически важен: Spring Security будет использовать его
    // для поиска пользователя в базе по логину во время авторизации.
    Optional<User> findByUsername(String username);

    // Можно добавить проверку на существование, чтобы не плодить дубликаты при регистрации
    Boolean existsByUsername(String username);
}