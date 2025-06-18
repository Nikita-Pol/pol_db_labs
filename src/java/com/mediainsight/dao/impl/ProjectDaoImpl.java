package com.mediainsight.dao.impl;

import com.mediainsight.dao.ProjectDao;
import com.mediainsight.model.Project;
import com.mediainsight.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectDaoImpl implements ProjectDao {

    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT id, name, description, created_at, user_id FROM project";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                project.setDescription(rs.getString("description"));

                // Конвертуємо DATETIME в LocalDateTime
                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    project.setCreatedAt(timestamp.toLocalDateTime());
                }

                project.setUserId(rs.getInt("user_id"));
                projects.add(project);
            }
        } catch (SQLException e) {
            System.err.println("Помилка при отриманні всіх проектів: " + e.getMessage());
            e.printStackTrace();
        }

        return projects;
    }

    @Override
    public Optional<Project> findById(int id) {
        String sql = "SELECT id, name, description, created_at, user_id FROM project WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Project project = new Project();
                    project.setId(rs.getInt("id"));
                    project.setName(rs.getString("name"));
                    project.setDescription(rs.getString("description"));

                    // Конвертуємо DATETIME в LocalDateTime
                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        project.setCreatedAt(timestamp.toLocalDateTime());
                    }

                    project.setUserId(rs.getInt("user_id"));
                    return Optional.of(project);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку проекту по ID: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Project> findByName(String name) {
        String sql = "SELECT id, name, description, created_at, user_id FROM project WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Project project = new Project();
                    project.setId(rs.getInt("id"));
                    project.setName(rs.getString("name"));
                    project.setDescription(rs.getString("description"));

                    // Конвертуємо DATETIME в LocalDateTime
                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        project.setCreatedAt(timestamp.toLocalDateTime());
                    }

                    project.setUserId(rs.getInt("user_id"));
                    return Optional.of(project);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку проекту по імені: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Project> findByUserId(int userId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT id, name, description, created_at, user_id FROM project WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project();
                    project.setId(rs.getInt("id"));
                    project.setName(rs.getString("name"));
                    project.setDescription(rs.getString("description"));

                    // Конвертуємо DATETIME в LocalDateTime
                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        project.setCreatedAt(timestamp.toLocalDateTime());
                    }

                    project.setUserId(rs.getInt("user_id"));
                    projects.add(project);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку проектів по користувачеві: " + e.getMessage());
            e.printStackTrace();
        }

        return projects;
    }

    @Override
    public void save(Project project) {
        String sql = "INSERT INTO project (name, description, created_at, user_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());

            // Встановлюємо поточний час, якщо не встановлено
            LocalDateTime createdAt = project.getCreatedAt();
            if (createdAt == null) {
                createdAt = LocalDateTime.now();
                project.setCreatedAt(createdAt);
            }
            stmt.setTimestamp(3, Timestamp.valueOf(createdAt));

            stmt.setInt(4, project.getUserId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Створення проекту невдале, жодний рядок не був вставлений.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    project.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Створення проекту невдале, ID не отримано.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при збереженні проекту: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Project project) {
        String sql = "UPDATE project SET name = ?, description = ?, user_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());
            stmt.setInt(3, project.getUserId());
            stmt.setInt(4, project.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("Оновлення проекту невдале, проект з ID " + project.getId() + " не знайдений.");
            }
        } catch (SQLException e) {
            System.err.println("Помилка при оновленні проекту: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM project WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("Видалення проекту невдале, проект з ID " + id + " не знайдений.");
            } else {
                System.out.println("Проект з ID " + id + " успішно видалений.");
            }
        } catch (SQLException e) {
            System.err.println("Помилка при видаленні проекту: " + e.getMessage());
            e.printStackTrace();
        }
    }
}