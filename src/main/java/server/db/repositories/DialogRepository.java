package server.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import server.db.dto.DialogDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DialogRepository extends JpaRepository<DialogDB, Integer> {
    List<DialogDB> findAllByUserDBS_Login(String login);

}
