package org.duckdns.bitwatchu.app.user.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.duckdns.bitwatchu.app.user.domain.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryn {

    private final EntityManager em;

    public void save(UserEntity user){ em.persist(user);}

    public UserEntity findById(long id){ return em.find(UserEntity.class, id);}
}
