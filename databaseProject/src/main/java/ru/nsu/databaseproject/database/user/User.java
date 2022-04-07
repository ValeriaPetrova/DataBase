package ru.nsu.databaseproject.database.user;

import lombok.Getter;
import lombok.Setter;

public class User {
    @Getter @Setter
    private Integer userId;
    @Getter @Setter
    private String userName;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private Role userRole;
}
