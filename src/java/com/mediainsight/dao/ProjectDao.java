package com.mediainsight.dao;

import com.mediainsight.model.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectDao {
    Optional<Project> findById(int id);
    List<Project> findAll();
    void save(Project project);  // Changed from Project save() to void save()
    void update(Project project);
    void delete(int id);
    List<Project> findByUserId(int userId);
    Optional<Project> findByName(String name);
}