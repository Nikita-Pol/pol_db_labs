package com.mediainsight.controller;

import com.mediainsight.dao.RoleDao;
import com.mediainsight.dao.impl.RoleDaoImpl;
import com.mediainsight.model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    private final RoleDao roleDao;

    public RoleController() {
        this.roleDao = new RoleDaoImpl();
    }

    // GET /api/roles - отримати всі ролі
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        try {
            List<Role> roles = roleDao.findAll();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/roles/{id} - отримати роль по ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable int id) {
        try {
            Optional<Role> role = roleDao.findById(id);
            if (role.isPresent()) {
                return ResponseEntity.ok(role.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /api/roles/name/{name} - отримати роль по назві
    @GetMapping("/name/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        try {
            Optional<Role> role = roleDao.findByName(name);
            if (role.isPresent()) {
                return ResponseEntity.ok(role.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST /api/roles - створити нову роль
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        try {
            roleDao.save(role);
            if (role.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body(role);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT /api/roles/{id} - оновити роль
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable int id, @RequestBody Role role) {
        try {
            // Перевіряємо чи існує роль
            Optional<Role> existingRole = roleDao.findById(id);
            if (!existingRole.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            role.setId(id);
            roleDao.update(role);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE /api/roles/{id} - видалити роль
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable int id) {
        try {
            // Перевіряємо чи існує роль
            Optional<Role> existingRole = roleDao.findById(id);
            if (!existingRole.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            roleDao.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}