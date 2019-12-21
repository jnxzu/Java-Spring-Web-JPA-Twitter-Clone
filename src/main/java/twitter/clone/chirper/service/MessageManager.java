package twitter.clone.chirper.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import twitter.clone.chirper.domain.Message;

public interface MessageManager extends JpaRepository<Message, Integer> {
    <S extends Message> S save(S msg);

    void delete(Message msg);

    Message getOneById(int id);

    List<Message> findAll();

    // TODO
    // find by content custom query
}