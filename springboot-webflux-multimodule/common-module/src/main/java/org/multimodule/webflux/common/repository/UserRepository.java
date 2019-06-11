package org.multimodule.webflux.common.repository;

import org.multimodule.webflux.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	 
    User findByUsername(String username);
}
