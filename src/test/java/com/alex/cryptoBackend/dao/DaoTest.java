package com.alex.cryptoBackend.dao;

import com.alex.cryptoBackend.model.User;
import com.alex.cryptoBackend.model.UserState;
import com.alex.cryptoBackend.model.UserStatus;
import com.alex.cryptoBackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class DaoTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindUser() {
        User user = new User();
        user.setFirstName("Alexei");
        user.setLastName("Murinets");
        user.setUsername("leha7777");
        user.setEmail("Alexeika7a@gmail.com");
        user.setPassword("popins777");
        user.setState(UserState.ACTIVE);
        user.setStatus(UserStatus.BASIC);
        userRepository.save(user);
        User user1 = userRepository.findByUsername("leha7777").orElseThrow(() -> new IllegalArgumentException("No such user"));
        assertThat(user1.getEmail()).isEqualTo("Alexeika7a@gmail.com");
    }
}
