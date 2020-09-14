package com.vega.springit.service;

import com.vega.springit.Repository.RoleRepository;
import com.vega.springit.model.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name){
     return roleRepository.findByName(name);
    }
}
