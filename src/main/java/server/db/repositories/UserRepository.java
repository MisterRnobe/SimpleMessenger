package server.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import server.db.dto.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
