package repository;

import db.DatabaseConnection;
import model.User;
import repository.impl.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(User user) {

        String sql  = "INSERT INTO users (id,full_name,email,password_hash,salt,role) VALUES (?,?,?,?,?,?)";

        try (
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setObject(1, user.getId());
            statement.setString(2, user.getFullName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPasswordHash());
            statement.setString(5, user.getSalt());
            statement.setString(6, user.getRole().name());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {

        String sql = "SELECT id,full_name,email,password_hash,salt,role FROM users WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1,email);
            var resultSet = statement.executeQuery();
            if (resultSet.next()){
                User user = new User(
                        resultSet.getObject("id", java.util.UUID.class),
                        resultSet.getString("full_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password_hash"),
                        resultSet.getString("salt"),
                        model.enums.RoleUser.valueOf(resultSet.getString("role")
                        )
                );

                return Optional.of(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {

        String sql = "SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1,email);
            var resultSet = statement.executeQuery();

            if (resultSet.next()){
               return resultSet.getBoolean(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

}
