package org.alouastudios.easytagalogbackend.repository;

import org.alouastudios.easytagalogbackend.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
