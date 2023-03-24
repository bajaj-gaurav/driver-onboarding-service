package com.example.driveronboardingservice.service;


import com.example.driveronboardingservice.entity.Role;
import com.example.driveronboardingservice.exception.RoleNotFoundException;
import com.example.driveronboardingservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Function to check and get the role.
     * @param roleName
     * @return
     * @throws RoleNotFoundException
     */
    public Role getDriverRole(String roleName) throws RoleNotFoundException {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new RoleNotFoundException("Role not found");
        }
        return role;
    }


}
