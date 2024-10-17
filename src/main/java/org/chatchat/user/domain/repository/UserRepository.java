package org.chatchat.user.domain.repository;

import org.chatchat.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String name);

    boolean existsByEmail(String email);

    boolean existsByUsername(String name);

    List<User> findByEmailContaining(String email);
}
