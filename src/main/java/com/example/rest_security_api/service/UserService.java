package com.example.rest_security_api.service;

import com.example.rest_security_api.dto.UserCreateDto;
import com.example.rest_security_api.dto.UserReadDto;
import com.example.rest_security_api.entity.User;
import com.example.rest_security_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public List<UserReadDto> findAll() {
        return null;
//        userRepository.; //ЧЗХ
    }

    public Optional<UserReadDto> getById(Integer id) {
        userRepository.getReferenceById(id);
        return null;
    }

    public UserReadDto create(UserReadDto user) {
//        userRepository.save(user); ///// Думать
        return null;
    }

    public Optional<UserReadDto> update(Integer userId, UserCreateDto user) {
        return null; // ??

    }

    public boolean delete(Integer id) {
        User user = userRepository.getReferenceById(id);
        userRepository.delete(user);
        return true; //временно
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
