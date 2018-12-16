package server.db.repositories;

import server.db.dto.UnreadMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnreadMessagesRepository extends CrudRepository<UnreadMessage,Integer> {
    Integer countByLoginAndMessageIdIn(String login, Integer messageId);
    void deleteByLoginAndMessageId(String login, Integer messageId);
}
