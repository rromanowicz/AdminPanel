package ex.rr.adminpanel.data.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    void deleteByUsername(String username);

    Optional<User> findByUsername(String username);
}
