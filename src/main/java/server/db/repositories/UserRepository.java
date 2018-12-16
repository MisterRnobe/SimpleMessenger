package server.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.db.dto.UserDB;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDB, String> {
    Optional<UserDB> findTopByLoginAndPassword(String login, String password);
    List<UserDB> findAllByLoginIn(List<String> list);
}
