package com.mediainsight.dao;

import com.mediainsight.model.MediaContent;
import java.util.List;
import java.util.Optional;

public interface MediaContentDao {

    /**
     * Зберегти новий медіа-контент
     */
    void save(MediaContent mediaContent);

    /**
     * Оновити існуючий медіа-контент
     */
    void update(MediaContent mediaContent);

    /**
     * Видалити медіа-контент по ID
     */
    void delete(int id);

    /**
     * Знайти медіа-контент по ID
     */
    Optional<MediaContent> findById(int id);

    /**
     * Знайти медіа-контент по назві (може бути кілька)
     */
    List<MediaContent> findByTitle(String title);

    /**
     * Знайти медіа-контент по типу
     */
    List<MediaContent> findByType(String type);

    /**
     * Знайти медіа-контент по проекту
     */
    List<MediaContent> findByProjectId(int projectId);

    /**
     * Знайти медіа-контент по автору
     */
    List<MediaContent> findByAuthorId(int authorId);

    /**
     * Знайти медіа-контент по статусу
     */
    List<MediaContent> findByStatus(String status);

    /**
     * Пошук медіа-контенту по ключовому слову
     */
    List<MediaContent> searchByKeyword(String keyword);

    /**
     * Отримати весь медіа-контент
     */
    List<MediaContent> findAll();

    /**
     * Отримати медіа-контент з пагінацією
     */
    List<MediaContent> findAll(int offset, int limit);

    /**
     * Підрахувати загальну кількість медіа-контенту
     */
    int count();
}