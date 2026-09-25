package util;

import model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserInputs {

    public static void mapToSetUser(PreparedStatement statement, User user) throws SQLException {
        statement.setObject(1, user.getId());
        statement.setString(2, user.getFullName());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPasswordHash());
        statement.setString(5, user.getSalt());
        statement.setString(6, user.getRole().name());
    }
}
