package repository;

import db.DatabaseConnection;
import model.User;
import repository.impl.UserRepository;
import util.UserInputs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcUserRepository implements UserRepository {
    Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(User user) {
        String sql  = "INSERT INTO users (id,full_name,email,password_hash,salt,role) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            UserInputs.mapToSetUser(statement,user);
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
                        resultSet.getObject("id", UUID.class),
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
        String sql = "SELECT * FROM users WHERE role = 'client'";
        try (Statement statement = connection.createStatement()){

        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public boolean updateProfile(UUID userId, String fullName, String email){
        String sql = "UPDATE users SET full_name = ?, email = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setObject(3, userId);
            int rows = preparedStatement.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
