package com.libapp.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libapp.backend.dto.CurrentUserSummaryDTO;
import com.libapp.backend.dto.UserUpdateRequestDTO;
import com.libapp.backend.entity.Role;
import com.libapp.backend.entity.RoleName;
import com.libapp.backend.entity.User;
import com.libapp.backend.exception.ResourceNotFoundException;
import com.libapp.backend.repository.RoleRepository;
import com.libapp.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public CurrentUserSummaryDTO getCurrentUserSummary(Long id) {
        User user = findUserById(id);
        return CurrentUserSummaryDTO.fromUser(user);
    }

    @Transactional
    public User updateUser(Long id, UserUpdateRequestDTO updateRequest) {
        User user = findUserById(id);
        Optional.ofNullable(updateRequest.getName()).ifPresent(user::setName);

        if (updateRequest.getRoleNames() != null && !updateRequest.getRoleNames().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : updateRequest.getRoleNames()) {
                Role role = roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase()))
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }
}