package com.mediainsight.controller;

import com.mediainsight.dao.ProjectDao;
import com.mediainsight.dao.impl.ProjectDaoImpl;
import com.mediainsight.model.Project;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectDao projectDao;

    public ProjectController() {
        this.projectDao = new ProjectDaoImpl();
    }

    // GET /api/projects - отримати всі проекти
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            List<Project> projects = projectDao.findAll();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/projects/{id} - отримати проект по ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable int id) {
        try {
            Optional<Project> project = projectDao.findById(id);
            if (project.isPresent()) {
                return ResponseEntity.ok(project.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/projects/name/{name} - отримати проект по назві
    @GetMapping("/name/{name}")
    public ResponseEntity<Project> getProjectByName(@PathVariable String name) {
        try {
            Optional<Project> project = projectDao.findByName(name);
            if (project.isPresent()) {
                return ResponseEntity.ok(project.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/projects/user/{userId} - отримати проекти користувача
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Project>> getProjectsByUser(@PathVariable int userId) {
        try {
            List<Project> projects = projectDao.findByUserId(userId);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST /api/projects - створити новий проект
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        try {
            // Встановлюємо поточний час створення, якщо не вказано
            if (project.getCreatedAt() == null) {
                project.setCreatedAt(LocalDateTime.now());
            }

            projectDao.save(project);
            if (project.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body(project);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT /api/projects/{id} - оновити проект
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable int id, @RequestBody Project project) {
        try {
            // Перевіряємо чи існує проект
            Optional<Project> existingProject = projectDao.findById(id);
            if (!existingProject.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            project.setId(id);
            // Зберігаємо оригінальну дату створення
            project.setCreatedAt(existingProject.get().getCreatedAt());
            projectDao.update(project);
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE /api/projects/{id} - видалити проект
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable int id) {
        try {
            // Перевіряємо чи існує проект
            Optional<Project> existingProject = projectDao.findById(id);
            if (!existingProject.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            projectDao.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}