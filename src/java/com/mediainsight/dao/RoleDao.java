package com.mediainsight.dao;

import com.mediainsight.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleDao {
    Optional<Role> findById(int id);
    List<Role> findAll();
    void save(Role role);
    void update(Role role);
    void delete(int id);
    Optional<Role> findByName(String name);
}