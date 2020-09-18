package com.vega.springit.Repository;

import com.vega.springit.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {

    //ON
    Role findByName(String name);
}
