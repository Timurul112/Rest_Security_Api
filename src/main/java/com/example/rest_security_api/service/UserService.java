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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;



    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream().map(userReadMapper::mapToDto).toList();
    }

    public Optional<UserReadDto> getById(Integer id) {
        return userRepository.findById(id).map(userReadMapper::mapToDto);
    }


    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        User saveUser = userCreateEditMapper.mapToDto(userDto);
        saveUser.setRole(Role.USER);
        saveUser.setStatus(Status.ACTIVE);
        return userReadMapper.mapToDto(userRepository.save(saveUser));
    }

    @Transactional
    public Optional<UserReadDto> update(Integer userId, UserCreateEditDto userDto) {
        return userRepository.findById(userId)
                .map(user -> userCreateEditMapper.copy(userDto, user))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::mapToDto);
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

        } else throw new RuntimeException("нет такого пользователя");
//        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User does not exist"));
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
