package utils;

import model.User;

import java.util.UUID;

public class UserGenerator {

    public static User getUniqueUser() {
        String uniqueId = UUID.randomUUID().toString();

        return new User(
                "test_" + uniqueId + "@mail.ru",
                "Password123",
                "TestUser"
        );
    }
}