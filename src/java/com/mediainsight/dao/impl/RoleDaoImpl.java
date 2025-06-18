package com.mediainsight.dao.impl;

import com.mediainsight.dao.RoleDao;
import com.mediainsight.model.Role;
import com.mediainsight.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleDaoImpl implements RoleDao {

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT id, name, description FROM role";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setName(rs.getString("name"));
                role.setDescription(rs.getString("description"));
                roles.add(role);
            }
        } catch (SQLException e) {
            System.err.println("Помилка при отриманні всіх ролей: " + e.getMessage());
            e.printStackTrace();
        }

        return roles;
    }

    @Override
    public Optional<Role> findById(int id) {
        String sql = "SELECT id, name, description FROM role WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setName(rs.getString("name"));
                    role.setDescription(rs.getString("description"));
                    return Optional.of(role);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку ролі по ID: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Role> findByName(String name) {
        String sql = "SELECT id, name, description FROM role WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setName(rs.getString("name"));
                    role.setDescription(rs.getString("description"));
                    return Optional.of(role);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку ролі по імені: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public void save(Role role) {
        // Перевіряємо, чи не існує вже роль з таким ім'ям
        Optional<Role> existingRole = findByName(role.getName());
        if (existingRole.isPresent()) {
            System.err.println("❌ Роль з ім'ям '" + role.getName() + "' вже існує!");
            // Встановлюємо ID існуючої ролі
            role.setId(existingRole.get().getId());
            return;
        }

        String sql = "INSERT INTO role (name, description) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, role.getName());
            stmt.setString(2, role.getDescription());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Створення ролі невдале, жодний рядок не був вставлений.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    role.setId(generatedKeys.getInt(1));
                    System.out.println("✓ Роль успішно збережена з ID: " + role.getId());
                } else {
                    throw new SQLException("Створення ролі невдале, ID не отримано.");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Помилка при збереженні ролі: " + e.getMessage());
            // Не встановлюємо ID, якщо збереження не вдалося
            role.setId(0);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Role role) {
        if (role.getId() <= 0) {
            System.err.println("❌ Неможливо оновити роль з некоректним ID: " + role.getId());
            return;
        }

        String sql = "UPDATE role SET name = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role.getName());
            stmt.setString(2, role.getDescription());
            stmt.setInt(3, role.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("❌ Оновлення ролі невдале, роль з ID " + role.getId() + " не знайдена.");
            } else {
                System.out.println("✓ Роль з ID " + role.getId() + " успішно оновлена.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Помилка при оновленні ролі: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM role WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("❌ Видалення ролі невдале, роль з ID " + id + " не знайдена.");
            } else {
                System.out.println("✓ Роль з ID " + id + " успішно видалена.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Помилка при видаленні ролі: " + e.getMessage());
            e.printStackTrace();
        }
    }
}