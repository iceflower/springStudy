package org.multimodule.common.repository;

import java.util.Optional;
import org.multimodule.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
}
