package twitter.clone.chirper.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import twitter.clone.chirper.domain.Message;

public interface MessageManager extends JpaRepository<Message, Integer> {
    <S extends Message> S save(S msg);

    void delete(Message msg);

    void deleteById(int id);

    @Transactional
    void deleteByAuthors_Id(int id);

    Message getOneById(int id);

    List<Message> findAll();

    List<Message> findByContentIgnoreCaseContaining(String search);

    List<Message> findByAuthors_Id(int id);
}