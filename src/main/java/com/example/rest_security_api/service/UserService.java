package com.example.rest_security_api.service;

import com.example.rest_security_api.dto.UserCreateDto;
import com.example.rest_security_api.dto.UserReadDto;
import com.example.rest_security_api.dto.UserUpdateDto;
import com.example.rest_security_api.entity.Role;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.entity.User;
import com.example.rest_security_api.mapper.UserCreateMapper;
import com.example.rest_security_api.mapper.UserReadMapper;
import com.example.rest_security_api.mapper.UserUpdateMapper;
import com.example.rest_security_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    private final UserUpdateMapper userUpdateMapper;


    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream().map(userReadMapper::mapToDto).toList();
    }

    public Optional<UserReadDto> getById(Integer id) {
        return userRepository.findById(id).map(userReadMapper::mapToDto);
    }


    @Transactional
    public UserReadDto create(UserCreateDto userCreateDto) {
        User saveUser = userCreateMapper.mapToEntity(userCreateDto);
        saveUser.setRole(Role.USER);
        saveUser.setStatus(Status.ACTIVE);
        return userReadMapper.mapToDto(userRepository.save(saveUser));
    }

    public UserReadDto updateOwnUser(UserUpdateDto userUpdateDto, Integer changeId, String authUsername) {
        User authUser = userRepository.findByUsername(authUsername).orElseThrow(() -> new RuntimeException("User does not exist"));
        if (!authUser.getId().equals(changeId)) {
            throw new RuntimeException("No access to user");
        } else {
            User updatedUser = userUpdateMapper.copy(userUpdateDto, authUser);
            User savedUpdateUser = userRepository.save(updatedUser);
            return userReadMapper.mapToDto(savedUpdateUser);
        }
    }

    public UserReadDto updateById(Integer changeId, UserUpdateDto userUpdateDto) {
        User user = userRepository.getById(changeId);
        User updatedUser = userUpdateMapper.copy(userUpdateDto, user);
        User savedUpdatedUser = userRepository.save(updatedUser);
        return userReadMapper.mapToDto(savedUpdatedUser);
    }


    @Transactional
    public boolean delete(Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    public User getByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();

        } else throw new RuntimeException("User does not exist");
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                )).orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user" + username));
    }
}
