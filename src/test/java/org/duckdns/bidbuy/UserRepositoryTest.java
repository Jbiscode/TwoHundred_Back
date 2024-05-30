package org.duckdns.bidbuy;

import jakarta.persistence.EntityManager;
import org.duckdns.bidbuy.app.user.domain.UserEntity;
import org.duckdns.bidbuy.app.user.repository.UserRepositoryn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepositoryn userRepositoryn;

    @Test
    @Rollback(false)
    @Transactional
    public void testUser() throws Exception {

        UserEntity user = new UserEntity("dfs","sdf","sdf","sdf","sdf");

        userRepositoryn.save(user);

//        UserEntity findUser = userRepositoryn.findById();

    }
}
