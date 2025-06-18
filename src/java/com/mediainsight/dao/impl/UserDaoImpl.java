package com.mediainsight.dao.impl;

import com.mediainsight.dao.UserDao;
import com.mediainsight.model.User;
import com.mediainsight.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT id, name, email, password, role_id FROM user WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку користувача по ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, email, password, role_id FROM user ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Помилка при отриманні всіх користувачів: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void save(User user) {
        // Перевіряємо, чи не існує вже користувач з таким email
        Optional<User> existingUser = findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            System.err.println("❌ Користувач з email '" + user.getEmail() + "' вже існує!");
            // Встановлюємо ID існуючого користувача
            user.setId(existingUser.get().getId());
            return;
        }

        String sql = "INSERT INTO user (name, email, password, role_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getRoleId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                        System.out.println("✓ Користувач успішно збережений з ID: " + user.getId());
                    }
                }
            } else {
                System.err.println("❌ Не вдалося зберегти користувача");
                user.setId(0);
            }
        } catch (SQLException e) {
            System.err.println("❌ Помилка при збереженні користувача: " + e.getMessage());
            user.setId(0);
        }
    }

    @Override
    public void update(User user) {
        if (user.getId() <= 0) {
            System.err.println("❌ Неможливо оновити користувача з некоректним ID: " + user.getId());
            return;
        }

        // Перевіряємо, чи не зайнятий email іншим користувачем
        Optional<User> existingUser = findByEmail(user.getEmail());
        if (existingUser.isPresent() && existingUser.get().getId() != user.getId()) {
            System.err.println("❌ Email '" + user.getEmail() + "' вже використовується іншим користувачем!");
            return;
        }

        String sql = "UPDATE user SET name = ?, email = ?, password = ?, role_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getRoleId());
            stmt.setInt(5, user.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✓ Користувач з ID " + user.getId() + " успішно оновлений.");
            } else {
                System.err.println("❌ Користувач з ID " + user.getId() + " не знайдений для оновлення.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Помилка при оновленні користувача: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM user WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("✓ Користувач з ID " + id + " успішно видалений.");
            } else {
                System.err.println("❌ Користувач з ID " + id + " не знайдений для видалення.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Помилка при видаленні користувача: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, name, email, password, role_id FROM user WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку користувача по email: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<User> findByRoleId(int roleId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, email, password, role_id FROM user WHERE role_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку користувачів по ролі: " + e.getMessage());
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRoleId(rs.getInt("role_id"));

        return user;
    }
}