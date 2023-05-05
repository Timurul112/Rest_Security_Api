package com.example.rest_security_api.service;

import com.example.rest_security_api.dto.UserCreateEditDto;
import com.example.rest_security_api.dto.UserReadDto;
import com.example.rest_security_api.entity.Role;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.entity.User;
import com.example.rest_security_api.mapper.UserCreateEditMapper;
import com.example.rest_security_api.mapper.UserReadMapper;
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

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private UserReadMapper userReadMapper;
    private UserCreateEditMapper userCreateEditMapper;


    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream().map(userReadMapper::map).toList();
    }

    public Optional<UserReadDto> getById(Integer id) {
        return userRepository.findById(id).map(userReadMapper::map);
    }


    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        User saveUser = userCreateEditMapper.map(userDto);
        saveUser.setRole(Role.USER);
        saveUser.setStatus(Status.ACTIVE);
        return userReadMapper.map(userRepository.save(saveUser));
    }

    @Transactional
    public Optional<UserReadDto> update(Integer userId, UserCreateEditDto userDto) {
        return userRepository.findById(userId)
                .map(user -> userCreateEditMapper.copy(userDto, user))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                )).orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
