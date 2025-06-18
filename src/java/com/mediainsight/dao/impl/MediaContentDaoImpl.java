package com.mediainsight.dao.impl;

import com.mediainsight.dao.MediaContentDao;
import com.mediainsight.model.MediaContent;
import com.mediainsight.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MediaContentDaoImpl implements MediaContentDao {

    @Override
    public void save(MediaContent mediaContent) {
        String sql = "INSERT INTO media_content (title, description, type, file_path, user_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, mediaContent.getTitle());
            stmt.setString(2, mediaContent.getDescription());
            stmt.setString(3, mediaContent.getType());
            stmt.setString(4, mediaContent.getFilePath());

            if (mediaContent.getUserId() != null) {
                stmt.setInt(5, mediaContent.getUserId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mediaContent.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при збереженні медіа контенту: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(MediaContent mediaContent) {
        String sql = "UPDATE media_content SET title = ?, description = ?, type = ?, file_path = ?, user_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mediaContent.getTitle());
            stmt.setString(2, mediaContent.getDescription());
            stmt.setString(3, mediaContent.getType());
            stmt.setString(4, mediaContent.getFilePath());

            if (mediaContent.getUserId() != null) {
                stmt.setInt(5, mediaContent.getUserId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setInt(6, mediaContent.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Помилка при оновленні медіа контенту: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM media_content WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Помилка при видаленні медіа контенту: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Optional<MediaContent> findById(int id) {
        String sql = "SELECT id, title, description, type, file_path, user_id FROM media_content WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MediaContent mediaContent = mapResultSetToMediaContent(rs);
                    return Optional.of(mediaContent);
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку медіа контенту по ID: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<MediaContent> findByTitle(String title) {
        List<MediaContent> mediaContents = new ArrayList<>();
        String sql = "SELECT id, title, description, type, file_path, user_id FROM media_content WHERE title LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + title + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mediaContents.add(mapResultSetToMediaContent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку медіа контенту по назві: " + e.getMessage());
            e.printStackTrace();
        }

        return mediaContents;
    }

    @Override
    public List<MediaContent> findByType(String type) {
        List<MediaContent> mediaContents = new ArrayList<>();
        String sql = "SELECT id, title, description, type, file_path, user_id FROM media_content WHERE type = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mediaContents.add(mapResultSetToMediaContent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку медіа контенту по типу: " + e.getMessage());
            e.printStackTrace();
        }

        return mediaContents;
    }

    @Override
    public List<MediaContent> findByProjectId(int projectId) {
        List<MediaContent> mediaContents = new ArrayList<>();
        String sql = """
            SELECT DISTINCT mc.id, mc.title, mc.description, mc.type, mc.file_path, mc.user_id
            FROM media_content mc
            JOIN task_content tc ON mc.id = tc.media_content_id
            JOIN analysis_task at ON tc.analysis_task_id = at.id
            WHERE at.project_id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mediaContents.add(mapResultSetToMediaContent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку медіа контенту по проекту: " + e.getMessage());
            e.printStackTrace();
        }

        return mediaContents;
    }

    @Override
    public List<MediaContent> findByAuthorId(int authorId) {
        List<MediaContent> mediaContents = new ArrayList<>();
        String sql = "SELECT id, title, description, type, file_path, user_id FROM media_content WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, authorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mediaContents.add(mapResultSetToMediaContent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку медіа контенту по автору: " + e.getMessage());
            e.printStackTrace();
        }

        return mediaContents;
    }

    @Override
    public List<MediaContent> findByStatus(String status) {
        // Оскільки в таблиці media_content немає поля status,
        // повертаємо порожній список
        System.out.println("Warning: findByStatus не підтримується - відсутнє поле status в таблиці media_content");
        return new ArrayList<>();
    }

    @Override
    public List<MediaContent> searchByKeyword(String keyword) {
        List<MediaContent> mediaContents = new ArrayList<>();
        String sql = "SELECT id, title, description, type, file_path, user_id FROM media_content WHERE title LIKE ? OR description LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mediaContents.add(mapResultSetToMediaContent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при пошуку медіа контенту по ключовому слову: " + e.getMessage());
            e.printStackTrace();
        }

        return mediaContents;
    }

    @Override
    public List<MediaContent> findAll() {
        List<MediaContent> mediaContents = new ArrayList<>();
        String sql = "SELECT id, title, description, type, file_path, user_id FROM media_content ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                mediaContents.add(mapResultSetToMediaContent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Помилка при отриманні всього медіа контенту: " + e.getMessage());
            e.printStackTrace();
        }

        return mediaContents;
    }

    @Override
    public List<MediaContent> findAll(int offset, int limit) {
        List<MediaContent> mediaContents = new ArrayList<>();
        String sql = "SELECT id, title, description, type, file_path, user_id FROM media_content ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mediaContents.add(mapResultSetToMediaContent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Помилка при отриманні медіа контенту з пагінацією: " + e.getMessage());
            e.printStackTrace();
        }

        return mediaContents;
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM media_content";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Помилка при підрахунку медіа контенту: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    private MediaContent mapResultSetToMediaContent(ResultSet rs) throws SQLException {
        MediaContent mediaContent = new MediaContent();
        mediaContent.setId(rs.getInt("id"));
        mediaContent.setTitle(rs.getString("title"));
        mediaContent.setDescription(rs.getString("description"));
        mediaContent.setType(rs.getString("type"));
        mediaContent.setFilePath(rs.getString("file_path"));

        // Перевіряємо на NULL для user_id
        int userId = rs.getInt("user_id");
        if (!rs.wasNull()) {
            mediaContent.setUserId(userId);
        }

        return mediaContent;
    }
}