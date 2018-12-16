package server.db.repositories;

import server.db.dto.MessageDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<MessageDB, Integer> {
    MessageDB findTopByDialogIdOrderByTimeDesc(Integer dialogId);
}
