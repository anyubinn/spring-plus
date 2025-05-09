package org.example.expert.domain.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserInsertTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void insertMillionUsers() {

        int totalUsers = 1000000;
        int batchSize = 10000;
        Set<String> nicknameSet = new HashSet<>();
        List<User> buffer = new ArrayList<>(batchSize);
        String targetNickname = "target_nickname_1234";

        for (int i = 1; i <= totalUsers; i++) {
            String nickname;
            do {
                nickname = "user_" + UUID.randomUUID().toString().substring(0, 8);
            } while (!nicknameSet.add(nickname));

            if (i == 777777) {
                nickname = targetNickname;
            }

            User user = new User("user" + i + "@test.com", "1234", nickname, UserRole.USER);
            buffer.add(user);

            if (i % batchSize == 0) {
                userRepository.saveAll(buffer);
                userRepository.flush();
                buffer.clear();
                System.out.println(i + " user insert success");
            }
        }

        if (!buffer.isEmpty()) {
            userRepository.saveAll(buffer);
            userRepository.flush();
        }

        System.out.println("million user insert success");
    }
}
