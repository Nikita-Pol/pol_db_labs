package com.mediainsight.controller;

import com.mediainsight.dao.MediaContentDao;
import com.mediainsight.dao.impl.MediaContentDaoImpl;
import com.mediainsight.model.MediaContent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/media-content")
@CrossOrigin(origins = "*")
public class MediaContentController {

    private final MediaContentDao mediaContentDao;

    public MediaContentController() {
        this.mediaContentDao = new MediaContentDaoImpl();
    }

    // GET /api/media-content - отримати весь медіа-контент
    @GetMapping
    public ResponseEntity<List<MediaContent>> getAllMediaContent() {
        try {
            List<MediaContent> content = mediaContentDao.findAll();
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/media-content/{id} - отримати медіа-контент по ID
    @GetMapping("/{id}")
    public ResponseEntity<MediaContent> getMediaContentById(@PathVariable int id) {
        try {
            Optional<MediaContent> content = mediaContentDao.findById(id);
            if (content.isPresent()) {
                return ResponseEntity.ok(content.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/media-content/title/{title} - отримати медіа-контент по назві
    @GetMapping("/title/{title}")
    public ResponseEntity<List<MediaContent>> getMediaContentByTitle(@PathVariable String title) {
        try {
            List<MediaContent> content = mediaContentDao.findByTitle(title);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/media-content/type/{type} - отримати медіа-контент по типу
    @GetMapping("/type/{type}")
    public ResponseEntity<List<MediaContent>> getMediaContentByType(@PathVariable String type) {
        try {
            List<MediaContent> content = mediaContentDao.findByType(type);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/media-content/project/{projectId} - отримати медіа-контент по проекту
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<MediaContent>> getMediaContentByProject(@PathVariable int projectId) {
        try {
            List<MediaContent> content = mediaContentDao.findByProjectId(projectId);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/media-content/author/{authorId} - отримати медіа-контент автора (по user_id)
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<MediaContent>> getMediaContentByAuthor(@PathVariable int authorId) {
        try {
            List<MediaContent> content = mediaContentDao.findByAuthorId(authorId);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/media-content/status/{status} - НЕ ПРАЦЮЄ (немає status в БД)
    @GetMapping("/status/{status}")
    public ResponseEntity<String> getMediaContentByStatus(@PathVariable String status) {
        return ResponseEntity.badRequest()
                .body("Status filtering not supported - status field not available in database schema");
    }

    // GET /api/media-content/search?q={query} - пошук медіа-контенту
    @GetMapping("/search")
    public ResponseEntity<List<MediaContent>> searchMediaContent(@RequestParam String q) {
        try {
            List<MediaContent> content = mediaContentDao.searchByKeyword(q);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST /api/media-content - створити новий медіа-контент
    @PostMapping
    public ResponseEntity<MediaContent> createMediaContent(@RequestBody MediaContent mediaContent) {
        try {
            // Валідація обов'язкових полів
            if (mediaContent.getTitle() == null || mediaContent.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (mediaContent.getType() == null || mediaContent.getType().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            mediaContentDao.save(mediaContent);
            if (mediaContent.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body(mediaContent);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT /api/media-content/{id} - оновити медіа-контент
    @PutMapping("/{id}")
    public ResponseEntity<MediaContent> updateMediaContent(@PathVariable int id, @RequestBody MediaContent mediaContent) {
        try {
            // Перевіряємо чи існує медіа-контент
            Optional<MediaContent> existingContent = mediaContentDao.findById(id);
            if (!existingContent.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Валідація обов'язкових полів
            if (mediaContent.getTitle() == null || mediaContent.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (mediaContent.getType() == null || mediaContent.getType().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            mediaContent.setId(id);
            mediaContentDao.update(mediaContent);
            return ResponseEntity.ok(mediaContent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE /api/media-content/{id} - видалити медіа-контент
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaContent(@PathVariable int id) {
        try {
            // Перевіряємо чи існує медіа-контент
            Optional<MediaContent> existingContent = mediaContentDao.findById(id);
            if (!existingContent.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            mediaContentDao.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST /api/media-content/{id}/publish - НЕ ПРАЦЮЄ (немає status та published_at в БД)
    @PostMapping("/{id}/publish")
    public ResponseEntity<String> publishMediaContent(@PathVariable int id) {
        return ResponseEntity.badRequest()
                .body("Publish functionality not supported - status and published_at fields not available in database schema");
    }

    // POST /api/media-content/{id}/unpublish - НЕ ПРАЦЮЄ (немає status в БД)
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<String> unpublishMediaContent(@PathVariable int id) {
        return ResponseEntity.badRequest()
                .body("Unpublish functionality not supported - status field not available in database schema");
    }

    // GET /api/media-content/paginated?page={page}&size={size} - отримати медіа-контент з пагінацією
    @GetMapping("/paginated")
    public ResponseEntity<List<MediaContent>> getMediaContentPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            int offset = page * size;
            List<MediaContent> content = mediaContentDao.findAll(offset, size);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/media-content/count - отримати кількість медіа-контенту
    @GetMapping("/count")
    public ResponseEntity<Integer> getMediaContentCount() {
        try {
            int count = mediaContentDao.count();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}