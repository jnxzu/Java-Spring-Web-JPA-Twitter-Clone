package twitter.clone.chirper.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import twitter.clone.chirper.domain.ChirperUser;

public interface UserManager extends JpaRepository<ChirperUser, Integer> {
    <S extends ChirperUser> S save(S user);

    ChirperUser findByNick(String nick);

    ChirperUser findByNickIgnoreCaseContaining(String nick);

    ChirperUser findByEmail(String email);

    void delete(ChirperUser user);

    ChirperUser findById(int id);

    ChirperUser getOneById(int id);

    List<ChirperUser> findAll();
}